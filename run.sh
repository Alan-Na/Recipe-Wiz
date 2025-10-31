#!/bin/bash
set -euo pipefail

PORT=${PORT:-8080}

cleanup() {
  echo "Stopping backend and frontend..."
  [[ -n "${BACKEND_PID:-}" ]] && kill ${BACKEND_PID} 2>/dev/null || true
  [[ -n "${FRONTEND_PID:-}" ]] && kill ${FRONTEND_PID} 2>/dev/null || true
}

trap cleanup EXIT

if lsof -ti :$PORT > /dev/null; then
  echo "Port $PORT is in use. Killing process $(lsof -ti :$PORT)"
  kill $(lsof -ti :$PORT)
fi

if lsof -ti :5173 > /dev/null; then
  echo "Port 5173 is in use. Killing process $(lsof -ti :5173)"
  kill $(lsof -ti :5173)
fi

( cd backend && mvn spring-boot:run ) &
BACKEND_PID=$!
echo "Backend started with PID ${BACKEND_PID}"

sleep 5

( cd frontend && npm run dev ) &
FRONTEND_PID=$!
echo "Frontend started with PID ${FRONTEND_PID}"

echo "Backend running on http://localhost:${PORT}"
echo "Frontend running on http://localhost:5173"

echo "Waiting for frontend dev server to be ready..."
# 循环检查 5173 端口，直到它返回一个 HTTP 响应
# -s = silent (静默模式), --fail = (连接失败时退出并返回错误码)
while ! curl -s --fail http://localhost:5173 > /dev/null; do
  echo "Waiting..."
  sleep 1
done

echo "Frontend is ready! Opening in browser."
open http://localhost:5173
# --- 新增代码结束 ---

wait
