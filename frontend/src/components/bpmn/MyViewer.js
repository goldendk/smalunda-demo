
import { useEffect } from 'react';
import { loadBpmn } from '../../services/BpmnRepository';
import BpmnViewer from 'bpmn-js/lib/NavigatedViewer';

export default function MyViewer({ taskFlowReport }) {

  useEffect(() => {


    const bpmnViewer = new BpmnViewer({
      container: '#canvas',

      /* uncomment to configure defaults for all overlays
      overlays: {
        defaults: {
          show: { minZoom: 1 },
          scale: true
        }
      }
      */
    });

    loadBpmn()
      .then((bpmnResult) => {
        console.log("Diagram length: " + bpmnResult.length);

        bpmnViewer.importXML(bpmnResult)
          .then(() => {
            var canvas = bpmnViewer.get('canvas'),
              overlays = bpmnViewer.get('overlays');

            taskFlowReport.taskReports.forEach((v) => {
              addOverlay(v, overlays);
            }
            );

            // zoom to fit full viewport
            canvas.zoom('fit-viewport');
          });

      });

  }, [taskFlowReport.taskReports]);



  function addOverlay(taskReport, overlays) {
    overlays.add(taskReport.taskId, 'note', {
      position: {
        top: -18,
        right: 78
      },
      html: `<div>
                <span class="badge state-badge initial-badge" data-state-id="${taskReport.taskId}" data-badge="${taskReport.initial.length}"></span>
                <span class="badge state-badge running-badge" data-state-id="${taskReport.taskId}" data-badge="${taskReport.running.length}"></span>
                <span class="badge state-badge retrying-badge" data-state-id="${taskReport.taskId}" data-badge="${taskReport.retrying.length}"></span>
             </div>`

    });

  }

}