
'use strict';
var express = require('express');
var app = express();
var catalyst = require('zcatalyst-sdk-node');
app.use(express.json());

app.post('/triggerCircuit', (req, res) => {

	var CatalystApp = catalyst.initialize(req);
	var body = req.body;
	console.log(body);
	let execName = new Date().getTime().toString();
	console.log(execName);
	let promiseResult = CatalystApp.circuit().execute({{{ENTER_YOUR_CIRCUITID_HERE}}},execName, body); //Provide the circuit ID of the circuit you create in the console

	promiseResult.then((functionResponse) => {
		console.log(functionResponse);
		res.status(200).send({ message: "Please check your email for the list of museums." });
	}).catch(err => {
		console.log(err); //You can view this log from Logs in the Catalyst console
		res.status(500).send({ message: "Please try again after sometime!" });
	})
});

module.exports = app;
