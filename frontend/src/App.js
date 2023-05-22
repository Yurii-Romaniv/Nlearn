import './App.css';
import Home from './Home';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import TestEdit from "./TestEdit";
import TestList from "./TestList";
import TestPassing from "./TestPassing";
import StudentsHome from "./StudentsHome";
import 'bootstrap/dist/css/bootstrap.css';

function App() {
    return (
        <BrowserRouter>
            <Switch>
                <Route path='/' exact={true} component={Home}/>
                <Route path='/teachers-home' exact={true} component={TestList}/>
                <Route path='/students-home' exact={true} component={StudentsHome}/>
                <Route path='/teachers-home/tests/:id' component={TestEdit}/>
                <Route path='/students-home/:id/start' component={TestPassing}/>
            </Switch>
        </BrowserRouter>
    )
}

export default App;