import React, {useState} from 'react';
import {useParams, useNavigate} from 'react-router-dom';
import {Button, Container, Form, FormGroup, Input} from 'reactstrap';
import {useQuery} from "react-query";
import Countdown from 'react-countdown';
import 'react-dropdown/style.css';
import {checkAuth} from "./checkAuth";


const emptyItem = {
    test: {
        name: '',
        duration: 1,
        id: null,
        endTime: "2050-05-22T22:08:03.5725112"
    },

    questions: [{
        questionText: "",
        id: 1,
        answerVariants: [""],
        correctIndexes: []
    }],
};

function TestPassing() {
    let navigate = useNavigate();
    const [item, setItem] = useState(emptyItem);
    const {id} = useParams();

    const {error, isLoading} = useQuery('fullTests', () =>
            fetch(`/students-home/${id}/start`, {mode: "no-cors"}).then(checkAuth),
        {
            onSuccess: (data) => {
                data.questions.forEach(q => q.correctIndexes = []);
                setItem(data);
            },
            retry: false,
        }
    );

    if (error) return <div>Request Failed</div>;
    if (isLoading) return <div>Loading...</div>;

    function handleCheckboxChange(id, sId, event) {
        let newItem = {...item};
        const target = event.target;
        let question = newItem.questions.find(q => q.id === +id);

        if (!target.checked) {
            (question.correctIndexes) = (question.correctIndexes).filter(item => item !== +sId);
            question.numberOfCorrectAnswers++;
        } else {
            if (!question.correctIndexes.includes(+sId) && question.numberOfCorrectAnswers) {
                (question.correctIndexes).push(+sId);
                question.numberOfCorrectAnswers--;
            }
        }
        setItem(newItem);
    }

    async function handleSubmit(event) {
        event.preventDefault();
        await fetch(`/students-home/${item.test.id}/end`, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item.questions),
        }).then(navigate("/"));
    }

    function isChecked(id, sIndex) {
        return (item.questions.find(q => q.id === +id)).correctIndexes.includes(sIndex);
    }

    return <div>
        <Container>
            {<h2>{item.test.name}</h2>}
            <Form onSubmit={handleSubmit} className="container-fluid">

                <Countdown date={new Date((item.test.endTime).replace('T', ' ').replace(/-/g, '/'))}
                           onComplete={handleSubmit} daysInHours={true}/>

                <hr></hr>
                {
                    item.questions.map((form, index) =>
                        <FormGroup key={form.id} className="my-4" style={{backgroundColor: "lightblue"}}>

                            {<h3 style={{fontWeight: "bold"}}>{form.questionText}</h3>}

                            {
                                form.answerVariants.map((answer, sIndex) =>
                                    <div key={sIndex.id}
                                         className="d-flex justify-content-center container my-auto col-11">
                                        <Input className="col-1" type="checkbox" name={form.id + ";" + sIndex}
                                               checked={isChecked(form.id, sIndex)} value={isChecked(form.id, sIndex)}
                                               onChange={(e) => handleCheckboxChange(form.id, sIndex, e)}/>

                                        {<p className="col-9 mx-2" style={{backgroundColor: "white", fontWeight: "normal"}}>{answer}</p>}
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