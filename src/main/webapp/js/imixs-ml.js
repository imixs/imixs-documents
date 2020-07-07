// imixs-ml scripts

IMIXS.namespace("com.imixs.workflow.office.ml");

var mlSearchInputID;
/**
 * Init Method for imixs-ml integration. 
 * The method runs only in case a imixsOfficeWorkflow.ml_json is defined.
 * The method highlights all ml input items to indicate if the value was a 
 * suggestion from the imixs-ml framework. (see MLAdapter class) 
 * 
 */
$(document).ready(function() {
	
	if (imixsOfficeWorkflow.ml_json) {
		fullItemList=$("input[data-item]");
		// hide the search result element
		$( "[id$=ml-search-results]" ).hide();
		
		// if suggest mode than update the style for input items asociated with ml.entities.
		if (imixsOfficeWorkflow.ml_json.status=='suggest') {
			// find all ml data items
			
			$(fullItemList).each(function(){			
				// select data-item value
				var dataItem=$(this).data('item');			
				// test if this data-item is a ml-item
				if (imixsOfficeWorkflow.ml_json.items.indexOf(dataItem)>-1) {
					// ok we have a ml item!
					$(this).addClass("imixs-ml");
				}
		    });
		}
		
		// add a keyup event for a search list....
		$(fullItemList).each(function(){			
			// select data-item value
			var itemName=$(this).data('item');			
			// add a keyup handler with delay to serach suggestions....
			$(this).keyup(delay(function (e) {
			  console.log('Time elapsed!', this.value);
			  mlSearchInputID=this.name;
			  console.log(' the id ware '+mlSearchInputID); 
			  imixsOfficeWorkflow.mlSearch({item: itemName, phrase: this.value });
			}, 500));
			
	    });
		
	}
	
});


function showMLSearchResult(data){
	var status = data.status;
    if (status === "success") {
	
	   // select the inital input element by its name...
	   var inputField = $('input[name ="' + mlSearchInputID + '"]') 
	
	
    	console.log('sth happened,  id=' + mlSearchInputID);
		console.log('sth val='  + $(inputField).val());
		
		// now we pull the result html list to this input field.....
		$( "[id$=ml-search-results]" ).insertAfter( inputField ).show();
		
	}
}

/*
 * delay function
 * see: https://stackoverflow.com/questions/1909441/how-to-delay-the-keyup-handler-until-the-user-stops-typing
 */
function delay(callback, ms) {
  var timer = 0;
  return function() {
    var context = this, args = arguments;
    clearTimeout(timer);
    timer = setTimeout(function () {
      callback.apply(context, args);
    }, ms || 0);
  };
}

