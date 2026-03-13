export const getWsUrl = (path: string): string => {
    const protocol = window.location.protocol === "https:" ? "wss" : "ws";

    const host = "localhost:8080";

    return `${protocol}://${host}${path}`;
};