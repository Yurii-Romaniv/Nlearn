import './App.css';
import Home from './Home';
import {BrowserRouter, Route, Switch} from 'react-router-dom';
import TestEdit from "./TestEdit";
import TestList from "./TestList";
import 'bootstrap/dist/css/bootstrap.css';

function App() {
    return (
        <BrowserRouter>
            <Switch>
                <Route path='/' exact={true} component={Home}/>
                <Route path='/teachersHome' exact={true} component={TestList}/>
                <Route path='/tests/:id' component={TestEdit}/>
            </Switch>
        </BrowserRouter>
    )
}

export default App;