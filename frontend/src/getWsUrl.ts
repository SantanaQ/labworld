export const getWsUrl = (path: string): string => {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const host = window.location.host;

    const cleanPath = path.startsWith('/') ? path : `/${path}`;

    return `${protocol}//${host}${cleanPath}`;
};