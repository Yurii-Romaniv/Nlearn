import React, {useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {Button, Container, Form, FormGroup, Input} from 'reactstrap';
import {useQuery} from "react-query";
import Countdown from 'react-countdown';
import 'react-dropdown/style.css';
import {checkAuth} from "./checkAuth";
import {Test} from "./entities/Test";

function TestPassing() {
    let navigate = useNavigate();
    const [test, setTest] = useState(new Test());
    const {id} = useParams();

    const {error, isLoading} = useQuery('fullTests', () =>
            fetch(`/students-home/${id}/start`, {mode: "no-cors"}).then(checkAuth),
        {
            onSuccess: (data) => {
                setTest(data);
            },
            retry: false,
        }
    );

    if (error) return <div>Request Failed</div>;
    if (isLoading) return <div>Loading...</div>;

    function handleCheckboxChange(index, sIndex, event) {
        let newTest = {...test};
        const target = event.target;
        newTest.questions[index].answerVariants[sIndex].isCorrect = target.checked;
        setTest(newTest);
    }

    async function handleSubmit() {
        await fetch(`/students-home/${test.id}/end`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(test.questions),
        }).then(navigate("/"));
    }

    return <div>
        <Container>
            {<h2>{test.name}</h2>}
            <Form onSubmit={handleSubmit} className="container-fluid">
                {test.endTime != null &&
                    <Countdown date={new Date((test.endTime).replace('T', ' ').replace(/-/g, '/'))}
                               onComplete={handleSubmit} daysInHours={true}/>
                }
                <hr></hr>
                {
                    test.questions.map((form, index) =>
                        <FormGroup key={form.id} className="my-4" style={{backgroundColor: "lightblue"}}>

                            {<h3 style={{fontWeight: "bold"}}>{form.questionText}</h3>}

                            {
                                form.answerVariants.map((answer, sIndex) =>
                                    <div key={answer.id}
                                         className="d-flex justify-content-center container my-auto col-11">
                                        <Input className="col-1" type="checkbox" name={form.id + ";" + sIndex}
                                               checked={answer.isCorrect} value={answer.isCorrect}
                                               onChange={(e) => handleCheckboxChange(index, sIndex, e)}/>

                                        {<p className="col-9 mx-2" style={{
                                            backgroundColor: "white",
                                            fontWeight: "normal"
                                        }}>{answer.answerText}</p>}
                                    </div>
                                )
                            }

                            <hr></hr>
                        </FormGroup>
                    )
                }
                <Button className="my-4" color="warning" type="submit">save and send test</Button>
            </Form>
        </Container>
    </div>
}

export default TestPassing;