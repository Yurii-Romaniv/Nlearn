import {Question} from "./Question";

export class Test {
    name = '';
    group = {
        name: null
    };
    duration = '';
    id = null;
    numberOfRetries = 1;
    endTime = null;
    questions = [new Question()];
}