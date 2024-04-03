//const diagramUrl: string = 'https://cdn.statically.io/gh/bpmn-io/bpmn-js-examples/dfceecba/starter/diagram.bpmn';
//const diagramUrl2: string = 'http://localhost:3000/resources/example.bpmn';
async function sendRequest(url: string, options: RequestInit | { method: any; headers?: undefined; body?: undefined; } | { method: any; headers: { 'Content-Type': string; }; body: string; }) {
    const response = await fetch(url, options);
    return await response.text();
}

async function sendRequestJson(url: string, options: RequestInit | { method: any; headers?: undefined; body?: undefined; } | { method: any; headers: { 'Content-Type': string; }; body: string; }) {
    const response = await fetch(url, options);
    return await response.json();
}

function createRequestOptions(method: string, body: null) {

    if (method === 'GET') {
        return {
            method
        }
    }

    else if (method === 'POST') {
        return {
            method,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body)
        }
    }

}
export async function loadBpmn(id: string): Promise<string> {
    var options = createRequestOptions('GET', null);
    return await sendRequest("http://localhost:8080/api/taskflow/bpmn/example-task-flow-id", options);
}

export interface TaskReport {
    taskId: string,
    running: number[],
    initial: number[],
    retrying: number[]
}

export interface TaskFlowReport {
    taskFlowId: string,
    taskReports: TaskReport[];
}

export async function getProcessStates(id: string): Promise<TaskFlowReport> {

    var data = await sendRequestJson("http://localhost:8080/api/taskflow/report/" + id, null);
    const report: TaskFlowReport = data as TaskFlowReport;
    return report;

}
