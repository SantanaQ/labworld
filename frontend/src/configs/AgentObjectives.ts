import {
    Pizza,
    Thermometer,
    Compass,
    ShieldAlert,
    RefreshCw,
} from "lucide-react";

export const objectiveConfig = {
    FIND_SUPPLY: {
        icon: Pizza,
        text: "Looking for resources",
    },
    ADJUST_TEMPERATURE: {
        icon: Thermometer,
        text: "Optimizing temperature",
    },
    EXPLORE_PATH: {
        icon: Compass,
        text: "Exploring the environment",
    },
    AVOID_DANGER: {
        icon: ShieldAlert,
        text: "Evading threats",
    },
    READJUST: {
        icon: RefreshCw,
        text: "Recalculating strategy",
    },
};