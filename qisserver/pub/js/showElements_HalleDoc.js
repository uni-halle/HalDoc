"use strict";

$("[id='printImg']").each(function(){
	var previewImage = $(this);
	previewImage.click(showElement);
});
$("[id='popupForm']").each(function(){
	var previewImage = $(this);
	var method = previewImage.attr("method");
	switch (method) {
	case "Kommission": {
		previewImage.click(showPopupKommission);
		break;
	}
	case "Gutachter": {
		previewImage.click(showPopupGutachter);
		break;
	}
	case "Gutachtermahnung": {
		previewImage.click(showPopupGutachtermahnung);
		break;
	}
	case "ListeEinsichtnahme": {
		previewImage.click(showPopupListeEinsichtnahme);
		break;
	}
	case "AushangVerteidigung": {
		previewImage.click(showPopupAushangVerteidigung);
		break;
	}
	case "Certificate": {
		previewImage.click(showPopupCertificate);
		break;
	}
	case "AppForCertificate": {
		previewImage.click(showPopupAppForCertificate);
		break;
	}
	}
});

function showElement(event) {
	// pfeilRunter pfeilRechts
	var previewImage = $(event.target);
	$("[id='printListe']").each(function(){
		var ele = $(this);
		var src = previewImage.attr("src");
		
		if(ele.css("display") == "none") {
			ele.css("display", "block");
			src = src.replace(/pfeilRechts/g, "pfeilRunter");
		} else {
			ele.css("display", "none");
			src = src.replace(/pfeilRunter/g, "pfeilRechts");
		}
		previewImage.attr("src", src);
	});
};

function getPopupDiv() {
	var popupFormDiv = $("<div></div>");
	popupFormDiv.attr("id", "popupFormDiv");
	popupFormDiv.attr("style","width:450px; min-height: 300px; float:left;position: absolute;top: 100px;left: 100px;z-index: 100;background-color: white;border-color: black;border-style: solid;border-width: thin;padding: 10px;");
	var closeButton = $("<button></button>");
	closeButton.attr("closeID", "popupFormDiv");
	closeButton.text("schließen");
	closeButton.click(closePopup);
	popupFormDiv.append(closeButton);
	popupFormDiv.append($("<br></br>"));
	return popupFormDiv;
}

function getFormular(legend, formElements, url) {
	var fieldset = $("<fieldset></fieldset>");
	fieldset.append($("<legend></legend>").append(legend));

	var popupForm = $("<form></form>");
	popupForm.attr("action", url);
	popupForm.attr("method", "post");
	popupForm.attr("style", "padding-top: 5px;");
	
	for (var i = 0; i < formElements.length; ++i){
		var element = formElements[i];
		if(element.label) {
			var labelComment = "";
			if(element.label.comment) {
				labelComment = element.label.comment;
			}
			var label = getLabel(element.field.id, element.label.text, labelComment);
			fieldset.append(label);
		}
		
		var fieldValue = "";
		if(element.field.value) {
			fieldValue = element.field.value;
		}
		switch(element.field.type) {
		case "select": {
			var form = getSelect(element.field.id, element.field.values);
			break;
		}
		case "radio": {
			var form = getRadio(element.field.id, element.field.values);
			break;
		}
		case "area": {
			var form = getTextarea(element.field.id, element.field.rows, fieldValue)
			break;
		}
		case "hidden": {
			var form = getHidden(element.field.id, fieldValue);
			break;
		}
		default: {
			var form = getInput(element.field.id, fieldValue);
		}
		}
		fieldset.append(form);
		fieldset.append($("<br></br>"));
		
	}

	var submit = $("<input></input>");
	submit.attr("type","submit");
	submit.attr("value","drucken");
	fieldset.append(submit);
	
	popupForm.append(fieldset);
	return popupForm;
}

function getSelectValuesFromHidden(method) {
	var array = new Array();
	$("[name='"+method+"_option']").each(function(){
		var id = $(this).attr("id");
		var value = $(this).attr("value");
		var object = {id: id, label: value};
		console.dir(object);
		array.push(object);
	});
	console.dir(array);
	return array;
}

function getFreitext(method, defaultText) {
	var freitext = defaultText;
	console.log(freitext);
	console.dir($("[name='"+method+"_freitext']"));
	var text = $("[name='"+method+"_freitext']").attr("value");
	console.log(text);
	if(text.trim().length > 0) {
		freitext = text;
	}
	return freitext;
}

function showPopupCertificate(event) {
	var popupFormDiv = getPopupDiv();
	var url = $(event.target).attr("link");
	var formElements = new Array(
			{label: {text: "im Auftrag von: "}, field: {id: "ia", type: "select", values: new Array({id: 'Din', label: 'der Dekanin'}, {id: 'D', label: 'des Dekans'}, {id: 'Dkin', label: 'der Geschäftsführenden Direktorin'}, {id: 'Dk', label: 'des Geschäftsführenden Direktors'})}},
			{label: {text: "Nachname: "}, field: {id: "nachname", type: "input"}},
			{label: {text: "Vorname: "}, field: {id: "vorname", type: "input"}},
			{label: {text: "Titel: "}, field: {id: "title", type: "input"}},
			//{label: {text: "Geschlecht: "}, field: {id: "gender", type: "radio", values: new Array({id: 'W', label: 'weiblich'}, {id: 'M', label: 'männlich'})}},
			{label: {text: "Sprache: "}, field: {id: "lang", type: "radio", values: new Array({id: 'de', label: 'deutsch'}, {id: 'en', label: 'englisch'})}}
	);
	var popupForm = getFormular("Angaben zum Zertifikat:", formElements, url);
	popupFormDiv.append(popupForm);
	$("body").append(popupFormDiv);
}

function showPopupAppForCertificate(event) {
	var popupFormDiv = getPopupDiv();
	var url = $(event.target).attr("link");
	var formElements = new Array(
			{label: {text: "Name des Rektorates: "}, field: {id: "rektorat", type: "input"}},
			{label: {text: "Titel und Name Rektor(in): "}, field: {id: "rektor", type: "input"}},
			{label: {text: "Titel und Name Dekan(in): "}, field: {id: "dekan", type: "input"}}
	);
	var popupForm = getFormular("Angaben zum Antrag für die Urkunde:", formElements, url);
	popupFormDiv.append(popupForm);
	$("body").append(popupFormDiv);
}

function showPopupAushangVerteidigung(event) {
	var popupFormDiv = getPopupDiv();
	var url = $(event.target).attr("link");
	var formElements = new Array(
			{label: {text: "Uhrzeit: "}, field: {id: "time", type: "input"}},
			{label: {text: "Ort: "}, field: {id: "place", type: "input", value: "HS / SR ... PLZ, Ort, Straße"}}
	);
	var popupForm = getFormular("Angaben zum Aushang:", formElements, url);
	popupFormDiv.append(popupForm);
	$("body").append(popupFormDiv);
}

function showPopupListeEinsichtnahme(event) {
	var popupFormDiv = getPopupDiv();
	var url = $(event.target).attr("link");
	var formElements = new Array(
			{label: {text: "ab: "}, field: {id: "from", type: "input"}},
			{label: {text: "bis: "}, field: {id: "to", type: "input"}}
	);
	var popupForm = getFormular("Angaben zur Einsichtnahme:", formElements, url);
	popupFormDiv.append(popupForm);
	$("body").append(popupFormDiv);
}

function showPopupGutachtermahnung(event) {
	var popupFormDiv = getPopupDiv();
	var url = $(event.target).attr("link");
	var method = $(event.target).attr("method");
	var selectArray = getSelectValuesFromHidden(method);
	var freitext = getFreitext(method, "da Sie Gutachter im Promotionsverfahren von $doktorand sind, erlaube ich mir bei Ihnen anzufragen, wann mit der Übersendung Ihres Gutachtens zu rechnen ist, damit wir das Promotionsverfahren entsprechend der Promotionsordnung weiterführen können.");
	var formElements = new Array(
		/*
			{label: {text: "Nachname: "}, field: {id: "nachname", type: "input"}},
			{label: {text: "Vorname: "}, field: {id: "vorname", type: "input"}},
			{label: {text: "Titel: "}, field: {id: "title", type: "input"}},
			{label: {text: "Straße: "}, field: {id: "street", type: "input"}},
			{label: {text: "PLZ: "}, field: {id: "plz", type: "input"}},
			{label: {text: "Ort: "}, field: {id: "ort", type: "input"}},
			{label: {text: "Geschlecht: "}, field: {id: "gender", type: "radio", values: new Array({id: 'W', label: 'weiblich'}, {id: 'M', label: 'männlich'})}},
		*/
			{label: {text: "Gutachter: "}, field: {id: "eval", type: "select", values: selectArray}},
			{label: {text: "Mahnungstext: ", comment: "Die Variable $doktorand wir durch 'Herrn Mustermann' bzw. 'Frau Musterfrau' ersetzt!"}, 
				field: {id: "text", type: "area", rows: 10, value: freitext}},
			//{label: {text: "Sprache: "}, field: {id: "lang", type: "select", values: new Array({id: 'de', label: 'deutsch'}, {id: 'en', label: 'englisch'})}}
			{field: {id: "lang", type: "hidden", value: "de"}}
	);
	var popupForm = getFormular("Angaben zum Gutachter:", formElements, url);
	popupFormDiv.append(popupForm);
	
	$("body").append(popupFormDiv);
}

function showPopupGutachter(event) {
	var popupFormDiv = getPopupDiv();
	var url = $(event.target).attr("link");
	var method = $(event.target).attr("method");
	var selectArray = getSelectValuesFromHidden(method);
	var formElements = new Array(
			/*
			{label: {text: "Nachname: "}, field: {id: "nachname", type: "input"}},
			{label: {text: "Vorname: "}, field: {id: "vorname", type: "input"}},
			{label: {text: "Titel: "}, field: {id: "title", type: "input"}},
			{label: {text: "Straße: "}, field: {id: "street", type: "input"}},
			{label: {text: "PLZ: "}, field: {id: "plz", type: "input"}},
			{label: {text: "Ort: "}, field: {id: "ort", type: "input"}},
			{label: {text: "Geschlecht: "}, field: {id: "gender", type: "radio", values: new Array({id: 'W', label: 'weiblich'}, {id: 'M', label: 'männlich'})}},
			//{label: {text: "Geschlecht: "}, field: {id: "gender", type: "select", values: new Array({id: 'W', label: 'weiblich'}, {id: 'M', label: 'männlich'})}},
			*/
			{label: {text: "Gutachter: "}, field: {id: "eval", type: "select", values: selectArray}},
			{label: {text: "Frist in Wochen: "}, field: {id: "time", type: "input"}},
			//{label: {text: "Sprache: "}, field: {id: "lang", type: "select", values: new Array({id: 'de', label: 'deutsch'}, {id: 'en', label: 'englisch'})}}
			{label: {text: "Sprache: "}, field: {id: "lang", type: "radio", values: new Array({id: 'de', label: 'deutsch'}, {id: 'en', label: 'englisch'})}}
	);
	var popupForm = getFormular("Angaben zum Gutachter:", formElements, url);
	popupFormDiv.append(popupForm);
	$("body").append(popupFormDiv);
}

function showPopupKommission(event) {
	var popupFormDiv = getPopupDiv();
	var url = $(event.target).attr("link");
	var formElements = new Array(
			{label: {text: "Nachname: "}, field: {id: "nachname", type: "input"}},
			{label: {text: "Vorname: "}, field: {id: "vorname", type: "input"}},
			{label: {text: "Titel: "}, field: {id: "title", type: "input"}},
			{label: {text: "Straße: "}, field: {id: "street", type: "input"}},
			{label: {text: "PLZ: "}, field: {id: "plz", type: "input"}},
			{label: {text: "Ort: "}, field: {id: "ort", type: "input"}},
			{label: {text: "Geschlecht: "}, field: {id: "gender", type: "radio", values: new Array({id: 'W', label: 'weiblich'}, {id: 'M', label: 'männlich'})}}
	);
	var popupForm = getFormular("Angaben zum Kommissionsmitglied:", formElements, url);
	popupFormDiv.append(popupForm);
	
	$("body").append(popupFormDiv);
};

function getLabel(forId, label, comment) {
	var labelEle = $("<label></label>");
	labelEle.attr("style", "display: inline-block; min-width: 8em;");
	labelEle.attr("title", comment);
	labelEle.attr("for", forId);
	labelEle.text(label);
	return labelEle
}

function getInput(id, value) {
	var inputEle = $("<input></input>");
	inputEle.attr("type", "text");
	inputEle.attr("id", id);
	inputEle.attr("name", id);
	inputEle.attr("value", value);
	inputEle.attr("style", "width: 22em");
	return inputEle
}

function getTextarea(id, rows, value) {
	var textEle = $("<textarea></textarea>");
	textEle.attr("id", id);
	textEle.attr("name", id);
	textEle.attr("cols", 35);
	textEle.attr("rows", rows);
	textEle.attr("style", "width: 400px;");
	textEle.append(value);
	return textEle;
}

function getHidden(id, value) {
	var inputEle = $("<input></input>");
	inputEle.attr("type", "hidden");
	inputEle.attr("id", id);
	inputEle.attr("name", id);
	inputEle.attr("value", value);
	return inputEle
}

function getSelect(id, array) {
	var inputEle = $("<select></select>");
	inputEle.attr("id", id);
	inputEle.attr("name", id);
	for (var i = 0; i < array.length; ++i){
		var ele = array[i];
		var optEle = $("<option></option>");
		optEle.attr("value", ele.id);
		optEle.text(ele.label);
		inputEle.append(optEle);
	}
	return inputEle
}

function getRadio(id, array) {
	var inputEle = $("<span></span>");
	for (var i = 0; i < array.length; ++i){
		var ele = array[i];
		var label = getLabel(id+"_"+ele.id, ele.label);
		label.css("padding-left","8px");
		label.css("min-width","2em");
		inputEle.append(label);
		var optEle = $("<input></input>");
		optEle.attr("id", id+"_"+ele.id);
		optEle.attr("name", id);
		optEle.attr("type", "radio");
		optEle.attr("value", ele.id);
		optEle.text(ele.label);
		inputEle.append(optEle);
	}
	return inputEle
}

function closePopup(event) {
	var button = $(event.target);
	var divId = button.attr("closeID");
	$("[id='"+divId+"']").remove();
}