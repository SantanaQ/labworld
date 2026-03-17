import React, {useState} from "react";
import Spinner from "./Spinner.tsx";

export interface FetchButtonProps {
    children: React.ReactNode,
    baseStyle : string,
    styleOnLoad : string,
    styleOnReady : string,
    onClick: () => Promise<void> | void,
    onError? : (error: unknown) => void,
}

function FetchButton(props: FetchButtonProps) {
    const [loading, setLoading] = useState(false);

    const handleClick = async () => {
        setLoading(true);
        try {
            await props.onClick();
        } catch (e) {
            props.onError ? props.onError(e) : console.error(e);
        } finally {
            setLoading(false);
        }
    }

    return (
      <button className={`${props.baseStyle} ${loading ? props.styleOnLoad : props.styleOnReady}`}
              onClick={handleClick}
              disabled={loading}
      >
          {loading ? (
              <Spinner />
          ) : (
              props.children
          )}
      </button>
    );
}

export default FetchButton;