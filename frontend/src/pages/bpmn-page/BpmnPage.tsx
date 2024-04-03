import React, { useEffect, useState } from "react";

import ViewerControl from '../../components/bpmn/ViewerControl'
import { TaskFlowReport, getProcessStates } from "../../services/BpmnRepository";
const MyViewer: any = require('../../components/bpmn/MyViewer').default;

export default function BpmnPage() {

  //  this.handleClick = this.handleClick.bind(this);

    const [taskFlowReport, setTaskFlowReport] = useState<TaskFlowReport | null>(null)


    useEffect(() => {
        getProcessStates("example-task-flow-id").then((result)=>{
            setTaskFlowReport(result);
        });
    }, [])

   const handleClick = () => {

        setTaskFlowReport({
            taskFlowId: "foo",
            taskReports: [
                {
                    taskId: "A",
                    running: [1],
                    initial: [2],
                    retrying: [3]
                }
            ]

        } as TaskFlowReport)


    }


    if (taskFlowReport == null) {
        return (<div>
            <div>
                Task flow report not loaded!
            </div>
            <input type="button" onClick={()=>handleClick()} value="Click me"/>

        </div>)
    }

    return (
        <div>

            <MyViewer taskFlowReport={taskFlowReport}></MyViewer>
            <ViewerControl taskFlowReport={taskFlowReport}></ViewerControl>
        </div>
    )
}