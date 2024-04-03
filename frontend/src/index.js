import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';

import 'bpmn-js/dist/assets/diagram-js.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css';
import BpmnPage from './pages/bpmn-page/BpmnPage'
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <div>
        <BpmnPage></BpmnPage>
    </div>
);

const handleDelegatedEvent = (event) => {
    // Check if the event target matches the criteria (e.g., button with id 'myButton')
    if (event.target && event.target.matches('.state-badge')) {
        // Find the React component and invoke the desired function
        var stateId = event.target.dataset["stateId"];


        var myEvent = new CustomEvent("badge-clicked", {
            detail:  {
                "stateId": stateId
            }
        });
        
        console.log("Activity clicked with activity id: " + stateId)
        window.dispatchEvent(myEvent);

    }
};


// Add an event listener to a parent element of the delegated target
document.addEventListener('click', handleDelegatedEvent);


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();