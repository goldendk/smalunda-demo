

class BpmnService3 {


    foo31() {
        return "foo31";
    }

    foo32() {
        return this.foo31() + "foo32";
    }


}

const bpmnService3 = new BpmnService3();


export default bpmnService3;

