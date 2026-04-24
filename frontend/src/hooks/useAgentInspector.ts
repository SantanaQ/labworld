import {useRef, useState, useCallback, useEffect} from "react";

type AgentInfo = {
    posX: number;
    posY: number;
    vX: number;
    vY: number;
    speed: number;
    energy: number;
    hunger: number;
    heat: number;
    curiosity: number;
    fear: number;
    objective: string;
};

type UseAgentInspectorProps = {
    engineRef: React.RefObject<any>;
    sessionId: string | undefined;
    simState: string;
};

export function useAgentInspector({
                                      engineRef,
                                      sessionId,
                                      simState,
                                  }: UseAgentInspectorProps) {

    const [agentInfo, setAgentInfo] = useState<AgentInfo | null>(null);
    const [loading, setLoading] = useState(false);

    const cacheRef = useRef<Map<string, AgentInfo>>(new Map());
    const abortRef = useRef<AbortController | null>(null);

    useEffect(() => {
        if (simState !== "paused") {
            setAgentInfo(null);
        }
    }, [simState]);

    const inspectAgentAt = useCallback(async (posX: number, posY: number) => {

        if (simState !== "paused") return;
        if (!engineRef.current) return;

        const agent = engineRef.current.findAgentByCoordinate(posX, posY);
        if (!agent) return;

        const agentId = agent.id;
        console.log(agentId);

        // cache hit?
        /*if (cacheRef.current.has(agentId)) {
            setAgentInfo(cacheRef.current.get(agentId)!);
            return;
        }*/

        // abort old request
        abortRef.current?.abort();
        const controller = new AbortController();
        abortRef.current = controller;

        try {
            setLoading(true);

            const response = await fetch(
                `/api/sim/agents/info/${sessionId}/${agentId}`,
                { signal: controller.signal }
            );

            if (!response.ok) {
                throw new Error("Failed to fetch agent info");
            }

            const data: AgentInfo = await response.json();

            // save in cache
            cacheRef.current.set(agentId, data);

            setAgentInfo(data);

        } catch (err: any) {
            if (err.name !== "AbortError") {
                console.error("Agent inspector error:", err);
            }
        } finally {
            setLoading(false);
        }

    }, [engineRef, sessionId, simState]);

    const clearSelection = useCallback(() => {
        setAgentInfo(null);
    }, []);

    return {
        agentInfo,
        loading,
        inspectAgentAt,
        clearSelection,
    };
}