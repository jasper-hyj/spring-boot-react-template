import React from "react";
import { createRoot } from "react-dom/client";

import reportWebVitals from "./reportWebVitals";
import Routes from "./Routes";

const root = createRoot(document.getElementById("root")!);

root.render(
    <React.StrictMode>
        <Routes />
    </React.StrictMode>
);

reportWebVitals();
