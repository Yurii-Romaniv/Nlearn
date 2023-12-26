import React from 'react';
import {Tab, TabList, TabPanel, Tabs} from 'react-tabs';
import {Container} from 'reactstrap';
import 'react-tabs/style/react-tabs.css';
import EntityList from "./EntityList";

export default function AdminsHome() {

    return (<div>
            <Container>
                <Tabs>
                    <TabList>
                        <Tab>Users</Tab>
                        <Tab>Tests</Tab>
                    </TabList>
                    <TabPanel>
                        <EntityList fetchUrl={"admins-home/users/"} entityName={"user"}/>
                    </TabPanel>
                    <TabPanel>
                        <EntityList fetchUrl={"teachers-home/tests/"} entityName={"test"}/>
                    </TabPanel>
                </Tabs>
            </Container>
        </div>

    );
}