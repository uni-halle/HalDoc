$("#add_att").click(function() {
	var button = $(this);
	var div = button.parent();
	var newID = parseInt(div.attr("newId"));
	var imagePath = div.attr("imagePath");
	console.log(imagePath);
	
	var newRow = $("#att_0").clone();
	newRow.attr("id","att_"+newID);
	var inputChild = newRow.children( "input" );
	inputChild.attr("name","mail_attach_" + newID);
	inputChild.attr("id","attach" + newID);
	var remButton = $("<button class='submit4mini' id='rem_att' style='background-color: #82A53E !important;'><img src='"+imagePath+"' /></button>");
	remButton.click(removeRow);
	newRow.append(remButton);
	
	$("#buttonDiv").before(newRow);
	
	div.attr("newId", newID + 1);
	return false;
});

function removeRow(event) {
	var button = $(this);
	button.parent().remove();
	return false;
}
