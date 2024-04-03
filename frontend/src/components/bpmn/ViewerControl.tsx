import React, { useEffect, useState } from "react";
import { TaskFlowReport, TaskReport } from "../../services/BpmnRepository";

// Define your component with inline props 
const ViewerControl: React.FC<{ taskFlowReport: TaskFlowReport }> = ({ taskFlowReport }) => {

  const [selected, setSelected] = useState<string | null>(null);

  useEffect(() => {
    // This code will run when the component mounts
    console.log('ViewerControl mounted');

    window.addEventListener('badge-clicked', handleCustomEvent);
    return () => {
      // This code will run when the component unmounts
      window.removeEventListener('badge-clicked', handleCustomEvent);
      console.log('ViewerControl unmounted');

    };
  }, []); // Empty dependency array means this effect runs only once, similar to componentDidMount


  function handleCustomEvent(evt: CustomEvent) {

    var clickedState: string = evt.detail.stateId;
    console.log("Event registered with detail: " + JSON.stringify(evt.detail));
    setSelected(clickedState);
  }


  if (taskFlowReport == null) {
    return (<div>No data (taskFlowReport).</div>);
  }
  if (selected == null) {
    return (<div>No badge selected.</div>);
  }

  var tReport: TaskReport = taskFlowReport.taskReports.filter(v => v.taskId === selected)[0];
  var arr2: number[] = [];

  if (tReport != null) {
    arr2 = tReport.initial;
  }



  return (
    <div>
      <p>Selected: {selected}</p>
      <p>Initial: {tReport.initial.length}</p>
      <p>Running: {tReport.running.length}</p>
      <p>Re-trying: {tReport.retrying.length}</p>
      <div>List of initial tasks.</div>
      <ul>
        {
          arr2.map((str, idx) =>
            (<li key={idx}>{str}</li>)

          )}

      </ul>
    </div>
  );
};
export default ViewerControl;