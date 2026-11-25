#!/usr/bin/env bash
###############################################################################
# start-backend.sh
#
# Starts the Job Board backend stack:
#   1. Frees TCP ports 8761 and 8762 (kills any process listening on them).
#   2. Starts Eureka Peer 1  (profile peer1, port 8761)
#   3. Starts Eureka Peer 2  (profile peer2, port 8762)
#   4. Waits until both Eureka ports are LISTEN.
#   5. Starts game-server (game-service), event-server, consultation-server.
#   6. Reports success/failure for every component and exits non-zero if
#      anything failed.
#
# Prerequisites on this host (verified): java 25, ./mvnw in each service dir,
# fuser / lsof / ss / curl / nohup / pgrep / pkill.
#
# Usage:
#   cd /home/yamaha/Desktop/jobBoard-4ssa13-g4
#   chmod +x Backend/start-backend.sh
#   ./Backend/start-backend.sh
#
# Logs are written to Backend/logs/<name>.log ; PIDs to Backend/logs/<name>.pid
###############################################################################
set -uo pipefail

# ---------------------------------------------------------------------------
# Configuration
# ---------------------------------------------------------------------------
ROOT_DIR="/home/yamaha/Desktop/jobBoard-4ssa13-g4"
BACKEND_DIR="${ROOT_DIR}/Backend"
LOG_DIR="${BACKEND_DIR}/logs"

EUREKA_PORTS=(8761 8762)
EUREKA_WAIT_TIMEOUT=120   # seconds, waiting for Eureka ports to open
HEALTH_WAIT=15            # seconds, waiting after microservice start

# Service definitions: name | maven working dir | extra mvn args | fixed port (or "")
declare -a SERVICES=(
  "eureka-peer1|${BACKEND_DIR}/eureka-server|-Dspring-boot.run.profiles=peer1|8761"
  "eureka-peer2|${BACKEND_DIR}/eureka-server|-Dspring-boot.run.profiles=peer2|8762"
  "game-server |${BACKEND_DIR}/game-service   ||"
  "event-server|${BACKEND_DIR}/event-server   ||"
  "consultation-server|${BACKEND_DIR}/consultation-service||"
)

mkdir -p "${LOG_DIR}"

# ---------------------------------------------------------------------------
# Tracking
# ---------------------------------------------------------------------------
FAILURES=0
declare -a STATUS_LINES=()

# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------
log() {
  printf '[%s] %s\n' "$(date '+%H:%M:%S')" "$*"
}

# Return 0 if port is free, 1 if something is listening.
port_is_free() {
  local port="$1"
  if fuser "${port}/tcp" >/dev/null 2>&1; then return 1; fi
  if lsof -ti:"${port}" >/dev/null 2>&1; then return 1; fi
  return 0
}

# Kill whatever is listening on a port, then verify it is free.
free_port() {
  local port="$1"
  local pids

  pids=$(fuser "${port}/tcp" 2>/dev/null | tr -s '[:space:]' ' ' | sed 's/^ *//;s/ *$//')
  if [[ -n "$pids" ]]; then
    log "[port ${port}] in use by PID(s): ${pids} — killing…"
    fuser -k "${port}/tcp" >/dev/null 2>&1 || true
    sleep 2
  fi

  pids=$(lsof -ti:"${port}" 2>/dev/null)
  if [[ -n "$pids" ]]; then
    log "[port ${port}] still held via lsof: ${pids} — killing…"
    printf '%s\n' "$pids" | xargs -r kill -9 >/dev/null 2>&1 || true
    sleep 2
  fi

  local tried=0
  while [[ $tried -lt 15 ]]; do
    if port_is_free "$port"; then
      log "[port ${port}] is free."
      return 0
    fi
    tried=$((tried + 1))
    sleep 1
  done

  log "[port ${port}] WARNING: still in use after waiting."
  return 1
}

# Wait until a TCP port is LISTEN (returns 0) or timeout (returns 1).
wait_for_port() {
  local port="$1"
  local timeout="$2"
  local tried=0
  while [[ $tried -lt $timeout ]]; do
    if ss -tln 2>/dev/null | grep -qE "(:${port}([[:space:]]|$))"; then
      return 0
    fi
    tried=$((tried + 1))
    sleep 1
  done
  return 1
}

# Check that a background Java/Maven process is still alive.
proc_alive() {
  local pid="$1"
  [[ -n "$pid" ]] && kill -0 "$pid" >/dev/null 2>&1
}

# Record a component status line for the final summary.
record() {
  local name="$1" status="$2" detail="${3:-}"
  STATUS_LINES+=("$(printf '%-20s %-8s %s' "$name" "$status" "$detail")")
  if [[ "$status" == "FAIL" ]]; then
    FAILURES=$((FAILURES + 1))
  fi
}

# Start one service detached with nohup. Returns 0 if launched & alive.
start_service() {
  local name="$1" dir="$2" mvn_args="$3" port="$4"
  local logfile="${LOG_DIR}/${name}.log"
  local pidfile="${LOG_DIR}/${name}.pid"

  log "=== Starting ${name} ==="
  log "  dir : ${dir}"
  log "  log : ${logfile}"

  # Launch detached; capture the shell's $! into the pid file.
  (
    cd "${dir}" || exit 1
    nohup ./mvnw spring-boot:run ${mvn_args} >"${logfile}" 2>&1 &
    echo $! >"${pidfile}"
  )

  local pid
  pid=$(cat "${pidfile}" 2>/dev/null)
  log "  PID : ${pid}"

  # Short grace period — Maven needs time to boot.
  sleep 5

  if ! proc_alive "$pid"; then
    log "  FAIL: ${name} exited shortly after start."
    tail -n 20 "${logfile}" 2>/dev/null | sed 's/^/    /'
    record "${name}" "FAIL" "process died (see ${logfile})"
    return 1
  fi

  # If a fixed port is expected, wait for it to open.
  if [[ -n "$port" ]]; then
    if wait_for_port "$port" "$EUREKA_WAIT_TIMEOUT"; then
      log "  OK  : port ${port} is LISTEN."
    else
      log "  WARN: port ${port} not LISTEN after ${EUREKA_WAIT_TIMEOUT}s (process alive: $(proc_alive "$pid" && echo yes || echo no))."
    fi
  fi

  record "${name}" "OK" "pid=${pid}"
  return 0
}
# ---------------------------------------------------------------------------
# Cleanup on interrupt / termination (kill everything we started)
# ---------------------------------------------------------------------------
cleanup() {
  log "=== Interrupted — stopping processes started by this script ==="
  local pf pid
  for pf in "${LOG_DIR}"/*.pid; do
    [[ -f "$pf" ]] || continue
    pid=$(cat "$pf" 2>/dev/null)
    if proc_alive "$pid"; then
      log "killing PID ${pid} (from ${pf})"
      kill "$pid" >/dev/null 2>&1 || true
    fi
  done
  exit 130
}
trap cleanup INT TERM

# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------
main() {
  log "Job Board backend starter"
  log "Root dir : ${ROOT_DIR}"
  log "Log dir  : ${LOG_DIR}"

  # 1. Free Eureka ports
  log "Step 1: ensuring Eureka ports 8761/8762 are free…"
  for port in "${EUREKA_PORTS[@]}"; do
    free_port "$port" || log "warning: could not fully free port ${port}"
  done

  # 2. Start Eureka peers
  log "Step 2: starting Eureka Peer 1 (8761) and Peer 2 (8762)…"
  start_service "eureka-peer1" "${BACKEND_DIR}/eureka-server" "-Dspring-boot.run.profiles=peer1" "8761"
  start_service "eureka-peer2" "${BACKEND_DIR}/eureka-server" "-Dspring-boot.run.profiles=peer2" "8762"

  # 3. Start microservices (random ports via server.port=0)
  log "Step 3: starting game-server, event-server, consultation-server…"
  start_service "game-server"     "${BACKEND_DIR}/game-service"     "" ""
  start_service "event-server"    "${BACKEND_DIR}/event-server"     "" ""
  start_service "consultation-server" "${BACKEND_DIR}/consultation-service" "" ""

  # 4. Health check: confirm all JVMs are still alive
  log "Step 4: health check (waiting ${HEALTH_WAIT}s for services to settle)…"
  sleep "${HEALTH_WAIT}"

  local entry name _dir _args _port pf pid
  for entry in "${SERVICES[@]}"; do
    IFS='|' read -r name _dir _args _port <<< "$entry"
    pf="${LOG_DIR}/${name}.pid"
    pid=$(cat "${pf}" 2>/dev/null)
    if proc_alive "$pid"; then
      log "  OK  : ${name} is running (PID ${pid})."
    else
      log "  FAIL: ${name} is NOT running. Last lines of ${LOG_DIR}/${name}.log:"
      tail -n 15 "${LOG_DIR}/${name}.log" 2>/dev/null | sed 's/^/    /'
      record "${name}" "FAIL" "process not alive (see ${LOG_DIR}/${name}.log)"
    fi
  done

  # 5. Summary
  echo
  echo "================================================================="
  echo "BACKEND STARTUP SUMMARY"
  echo "================================================================="
  printf '%-20s %-8s %s\n' "SERVICE" "STATUS" "DETAIL"
  printf '%-20s %-8s %s\n' "--------------------" "--------" "------"
  local line
  for line in "${STATUS_LINES[@]}"; do
    printf '%s\n' "$line"
  done
  echo "-----------------------------------------------------------------"
  if [[ $FAILURES -eq 0 ]]; then
    echo "RESULT: ALL SERVICES STARTED SUCCESSFULLY"
    echo "Logs:  tail -f ${LOG_DIR}/<name>.log"
    echo "-----------------------------------------------------------------"
    exit 0
  else
    echo "RESULT: ${FAILURES} SERVICE(S) FAILED TO START"
    echo "Check the corresponding .log files under ${LOG_DIR}"
    echo "-----------------------------------------------------------------"
    exit 1
  fi
}

main "$@"
