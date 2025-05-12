const socket = new WebSocket("ws://localhost:8080/risk");

socket.onopen = () => {
  console.log("Connectat al servidor");
};

socket.onerror = (error) => {
  console.error("Error de connexió:", error);
};

socket.onmessage = (event) => {
  console.log("Missatge rebut:", event.data);
};

export const sendMessage = (message) => {
  if (socket.readyState == WebSocket.OPEN) {
    socket.send(message);
  }
};

export default socket;