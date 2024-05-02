import React, { Suspense, useEffect, useState } from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { Spinner } from "reactstrap";

function LoadingSpinner(): JSX.Element {
    return (
        <div className="w-100 h-100 d-flex align-items-center justify-content-center">
            <Spinner animation="border" role="status" size="xl" />
        </div>
    );
}

const Routes: React.FC = () => {

    const router = createBrowserRouter([
        {
            // path: "/",
            // element: <Layout />,
            // children: [
            //     { path: "*", element: <NotFoundPage /> },
            // ],
        }
    ]);

    return <RouterProvider router={router} fallbackElement={<LoadingSpinner />} />;
};

export default Routes;
