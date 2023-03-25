import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import {Button, ButtonGroup, Container, Form, FormGroup, Input, Label} from 'reactstrap';
import AppNavbar from './AppNavbar';

class ClientEdit extends Component {
    maxId=1;

    handleChange(event) {

        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};

        console.log(value);
        console.log(target.name);

        if(name.includes(';') ){

             const indexes =name.split(";");

             if(indexes[1]) {
                 //console.log(  ((item.questions[indexes[0]]).answerVariants)[indexes[1]] );
                 ((item.questions[indexes[0]]).answerVariants)[indexes[1]] = value;
             }else{
                 (item.questions[indexes[0]]).question = value;
             }
        }else{

            item.test[name] = value;
        }






        this.setState({item});
    }


    async handleSubmit(event) {
        event.preventDefault();
         var {item} = this.state;
         item.added= Array.from(item.added);
        item.deleted= Array.from(item.deleted);
        await fetch('http://localhost:8080/subload/tests' + (item.test.id ? '/' + item.test.id : ''), {
            method: (item.test.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });
        //this.props.history.push('/teachersHome');
    }


    async remove(index, sIndex) {
            //let updatedForms = [...this.state.item].filter(i => i.id !== id);
            //this.setState({forms: updatedForms});
            let item = {...this.state.item};
            (item.questions[index]).answerVariants=  ((item.questions[index]).answerVariants).filter(i => i !== (item.questions[index]).answerVariants[sIndex]);
            this.setState({item});
    }


    async add(index) {
        //let updatedForms = [...this.state.item].filter(i => i.id !== id);
        //this.setState({forms: updatedForms});
        let item = {...this.state.item};
        (item.questions[index]).answerVariants.push([]);
        this.setState({item});
    }



    async addQuestion() {
        //let updatedForms = [...this.state.item].filter(i => i.id !== id);
        //this.setState({forms: updatedForms});
        let item = {...this.state.item};

        const que= {name:"", id:++(this.maxId), answerVariants:[""]};
        item.questions.push(que);
        item.added.add(que.id)
        //console.log(que.id);
        this.setState({item});
    }


    async delQuestion(index) {
        let item = {...this.state.item};
        let questionId=item.questions[index].id

        if(item.added.has(questionId)){
            item.added.delete(questionId);
        }else{
            item.deleted.add(questionId);
        }

        //console.log(item.deleted);
        //console.log(item.added);

        item.questions=  item.questions.filter(i => i !== item.questions[index]);
        this.setState({item});
    }


    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const newItem = (await (await fetch(`http://localhost:8080/subload/tests/${this.props.match.params.id}`)).json());

            //console.log(this.maxId);
            let pThis = this;
            newItem.questions.forEach(function(q) {
                if(q.id > pThis.maxId){
                    pThis.maxId=q.id;
                }
            });

            newItem.added=new Set();
            newItem.deleted=new Set();

            this.setState({item: newItem});
        }
    }

    emptyItem = {
        test:{
            name: '',
            id: ''
        },

        questions: [{
            answerVariants:[""]

            } ],

        added:new Set(),
        deleted:new Set()

    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }


    render() {
        const {item} = this.state;
        const forms= item.questions;
        const title = <h2>{item.test.id ? 'Edit Client' : 'Add Client'}</h2>;




        //console.log((forms[0]).answerVariants[0])

        const formList = forms.map((form, index) => {
            return <FormGroup key={form.id} className="my-4" style={{backgroundColor:"lightblue"}}>


                <Input className="col-9 bold" placeholder="name of question" type="text" name={index + ";"} value={form.question|| ''}  onChange={this.handleChange}  style={{fontWeight:"bold"} }/>


                {form.answerVariants.map((answer, sIndex) => {
                    return <div className="d-flex justify-content-center container my-auto col-11">

                    <Input className="col-9" type="text" name={index+ ";" +sIndex} value={answer|| ''}
                               onChange={this.handleChange}/>
                        <Button className="col-1" size="sm" color="danger" onClick={() => this.remove(index, sIndex)}>Delete</Button>
                    </div>

            })
                }

                <Button className="col-1" size="sm"  color="primary" onClick={() => this.add(index)}>add one</Button>
                <Button className="col-2" size="sm"  color="danger" onClick={() => this.delQuestion(index)}>delete question</Button>

                <hr></hr>


            </FormGroup>
        });







        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit} className="container-fluid">
                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input type="text" name="name" id="name" value={item.test.name || ''}
                               onChange={this.handleChange} autoComplete="name"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="duration">passing time(minutes)</Label>
                        <Input type="number" name="duration" id="duration" value={item.test.duration || ''}
                               onChange={this.handleChange} autoComplete="duration"/>
                    </FormGroup>

                    <hr></hr>
                    {formList}

                    <Button className="my-4" color="warning" onClick={() => this.addQuestion()} >one more question</Button>

                    <FormGroup className="my-4">
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/teachersHome">Cancel</Button>
                    </FormGroup>

                </Form>


            </Container>
        </div>
    }






}
//export default withRouter(ClientEdit);
export default ClientEdit;

/*
        <td>
            <ButtonGroup>
                <Button size="sm" color="primary" tag={Link} to={"/clients/" + form.id}>Edit</Button>
                <Button size="sm" color="danger" onClick={() => this.remove(form.id)}>Delete</Button>
            </ButtonGroup>
        </td>
        */