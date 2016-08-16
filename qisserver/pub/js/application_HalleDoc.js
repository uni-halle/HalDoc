$("#tabs").tabs();

$("[type='date']").each(function(){
//	console.log(window.chrome);
	if(!window.chrome) {
		var input = $(this);
		var oldDate = input.val()
		input.val(getFormatedDate(oldDate));
		input.focusout(function(event){
			var field = $(event.target);
			var oldDate = field.val();
			field.val(getFormatedDate(oldDate));
		})
	}
});

function getFormatedDate(oldDate) {
	var newDate = oldDate;
	if(oldDate.indexOf("-") > -1) {
		var yf=oldDate.split("-")[0];           
		var mf=oldDate.split("-")[1];
		var df=oldDate.split("-")[2];
		newDate = df + "." + mf + "." + yf;
	} else if(oldDate.indexOf("/") > -1) {
		var yf=oldDate.split("/")[2];
		var mf=oldDate.split("/")[0];
		var df=oldDate.split("/")[1];
		newDate = df + "." + mf + "." + yf;
	}
	console.log(oldDate + " -> " + newDate);
	return newDate;
}

$("#promoFaculty").change(function() {
	var selectEle = $(this);
	var selectedFakId = selectEle.val();
	var wantedFakId = selectEle.attr("showOn");
	var displayEleId = selectEle.attr("show");
	var displayEle = $("#"+displayEleId);
	if(wantedFakId === selectedFakId) {
		console.log("show " + displayEleId);
		displayEle.css("display", "");
	} else {
		console.log("hide " + displayEleId);
		displayEle.css("display", "none");
	}
});

$("#open_print").click(function() {
	var button = $(this);
	var form = $("#openForm2");
	if(form.length === 0) {
		form = $("#openForm");
	}
	var errorList = $( "ul.errorMessages", form );
	errorList.empty();
	errorList.parent().removeClass("ui-state-error");
	$("#errorHeader").css("display", "none");
	form.find( "input, select" ).each( function( index, node ) {
    	$(node).removeClass("ui-state-error");
	});
    // Find all invalid fields within the form.
    var invalidFields = form.find( ":invalid" ).each( function( index, node ) {
        // Find the field's corresponding label
    	$(node).addClass("ui-state-error");
        var label = $( "label[for=" + node.id + "] "),
            // Opera incorrectly does not fill the validationMessage property.
            message = node.validationMessage || 'Invalid value.';
        var tabId = $(node).parent().parent().attr("id");
        var tabName = $( "a[href='#" + tabId + "']");
        if(errorList.children().length == 0) {
        	errorList.parent().addClass("ui-state-error");
        	$("#errorHeader").css("display", "block");
        }
        var li = $("<li style='cursor: pointer' for='" + node.id + "' page='" + tabId + "'><span><b>" + tabName.html() + "</b>: <i>" + label.html() + "</i></span></li>");
        li.click(function(){
        	var li = $(this);
        	var tabNr = parseInt(li.attr("page").substring(li.attr("page").length - 1)) - 1;
        	$( "#tabs" ).tabs({ active: tabNr });
        	$( "#" + li.attr("for") ).focus();
        });
        errorList.show().append( li );
    });
    if(errorList.children().length == 0) {
    	form.submit();
    }
    return false;
});
$("[id='add']").each(function(){
	var button = $(this);
	button.click(addTableRow);
});
$("[id='del']").each(function(){
	var button = $(this);
	button.click(deleteTableRow);
});

$("input:radio[name='app_prog'], input:radio[name='app_binat'], input:radio[name='open_prog'], input:radio[name='open_binat'], input:radio[name='app_resTrGroup'], input:radio[name='app_statistic'], input:radio[name='app_prevPromo'], input:radio[name='open_resTrGroup'], input:radio[name='open_statistic'], input:radio[name='open_prevPromo'], input:radio[name='app_ethical'], input:radio[name='open_ethical']").each(function(){
	var radio = $(this);
	radio.change(showingDiv);
});

$("[name*='app_degree_']").each(function(){
	var button = $(this);
	button.change(showAddDegree);
});

$("[name='submitForm']").each(function(){
	var button = $(this);
	button.click(function(event) {
		var button = $(event.target);
		if(button.get(0).tagName == "IMG") {
			button = button.parent();
		}
		var applicationCount = button.attr("id");
		var myRadio = $("input[name='grad_"+applicationCount+"']");
		var value = myRadio.filter(':checked');
		if(value.length > 0) {
			$("[name='gradtext_"+applicationCount+"']").remove();
			$("[name='isfh_"+applicationCount+"']").remove();
			var text = value.attr("text");
			var input = $("<input></input>");
			input.attr("type","hidden");
			input.attr("name","gradtext_"+applicationCount);
			input.attr("value",text);
			var isfh = value.attr("isfh");
			var input2 = $("<input></input>");
			input2.attr("type","hidden");
			input2.attr("name","isfh_"+applicationCount);
			input2.attr("value",isfh);
			var grad = value.attr("value");
			var input3 = $("<input></input>");
			input3.attr("type","hidden");
			input3.attr("name","grad_"+applicationCount);
			input3.attr("value",grad);

			$( "#form_"+applicationCount ).append(input);
			$( "#form_"+applicationCount ).append(input2);
			$( "#form_"+applicationCount ).append(input3);
			$( "#form_"+applicationCount ).submit();
		} else {
			$("<div></div>").html("Bitte wählen Sie einen zu berücksichtigenden bisherigen Abschluss aus, um den Antrag übernehmen zu können!").dialog({
		        title: "Warnung",
		        resizable: false,
		        modal: true,
		        dialogClass:'ui-state-error',
		        buttons: {"Ok": function() {$( this ).dialog( "close" );}}
		    });
			return false;
		}
	});
});

$("[name='showDialog']").each(function(){
	var button = $(this);
	var dialogID = button.attr("for");
	$( "#"+dialogID ).dialog({
	      autoOpen: false,
	      width: 700,
	      modal: true,
	      show: {
	        effect: "fade",
	        duration: 100
	      },
	      hide: {
	        effect: "fade",
	        duration: 100
	      },
	      buttons: {"Ok": function() {$( this ).dialog( "close" );}}
	    });
	button.click(function(event) {
		var button = $(event.target);
		if(button.get(0).tagName == "IMG") {
			button = button.parent();
		}
    	var dialogID = button.attr("for");
    	$("#"+dialogID).dialog( "open" );
    });
});

function showAddDegree(event) {
	var select = $(event.target);
	var nameArr = select.attr("name").split("_");
	var additional = $("#"+nameArr[1]+"_add_"+nameArr[2]);
	console.dir(additional);
	if(select.val() == -1) {
		console.log(nameArr[2]);
		additional.attr("type","text");
		additional.focus();
	} else {
		additional.val("");
		additional.attr("type","hidden");
	}
}

function showingDiv(event) {
	var showingDiv = $(this).attr("show");
	if ($(this).is(':checked') && $(this).val() == 'j') {
		$("[id='"+showingDiv+"']").css("display","block");
		console.log("ja");
	} else {
		$("[id='"+showingDiv+"']").css("display","none");
		console.log("nein");
	}
}

function deleteTableRow(event) {
	var button = $(event.target);
	var number = parseInt(button.attr("nr"));
	$("[id='row_"+number+"']").remove();
	return false;
};

function addTableRow(event) {
	var button = $(event.target);
	var newNumber = parseInt(button.attr("nr"));
	var table = $("[id='acadGrades']");
	var newRow = createNewRow(newNumber);
	//var addRow = $("[id='addRow']");
	//$("[id='addRow']").remove();
	//table.append(newRow);
	//table.append(addRow);
	newRow.insertBefore("[id='addRow']");
	newNumber = newNumber + 1;
	button.attr("nr", newNumber);
	return false;
};

function createNewRow(newNumber) {
	var inputStyle = $("[id='when_1']").attr("style");

	var row = $("<tr></tr>");
	row.attr("id", "row_" + newNumber);
	var cell = $("<td></td>");
	var select = $("[id='degree_1']").clone();
	select.attr("id", "degree_" + newNumber);
	select.attr("name", "app_degree_" + newNumber);
	select.val("");
	select.change(showAddDegree);
	cell.append(select);
	cell.append($("<br></br>"));
	var input0 = $("<input></input>");
	input0.attr("id", "degree_add_" + newNumber);
	input0.attr("name", "app_degree_add_" + newNumber);
	input0.attr("type", "hidden");
	input0.attr("style", inputStyle);
	cell.append(input0);
	row.append(cell);

	var input1 = $("<input></input>");
	input1.attr("id", "when_" + newNumber);
	input1.attr("name", "app_when_" + newNumber);
	input1.attr("type", "text");
	input1.attr("style", inputStyle);
	row.append($("<td></td>").append(input1));

	var input2 = $("<input></input>");
	input2.attr("id", "where_" + newNumber);
	input2.attr("name", "app_where_" + newNumber);
	input2.attr("type", "text");
	input2.attr("style", inputStyle);
	row.append($("<td></td>").append(input2));
	
	var labelStyle = $("[id='fh_1_label']").attr("style");
	var label3 = $("<label></label>");
	label3.attr("for", "fh_" + newNumber);
	label3.attr("id", "fh_" + newNumber + "_label");
	label3.attr("style", labelStyle);
	label3.html("ja");
	var td = $("<td></td>");
	td.append(label3);
	var input3 = $("<input></input>");
	input3.attr("id", "fh_" + newNumber);
	input3.attr("name", "app_fh_" + newNumber);
	input3.attr("type", "checkbox");
	td.append(input3);
	row.append(td);

	var delButton = $("<button></button>");
	delButton.attr("id", "del");
	delButton.attr("nr", newNumber);
	delButton.attr("class", "submit4");
	delButton.html("löschen");
	delButton.click(deleteTableRow);
	row.append($("<td></td>").append(delButton));
	
	return row;
}
