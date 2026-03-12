export const getWsUrl = (path: string = '/ws'): string => {
    if (typeof window === 'undefined') return '';

    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const host = window.location.host;

    const envUrl = import.meta.env.VITE_WS_OVERRIDE;
    return envUrl || `${protocol}//${host}${path}`;
};