define(['jquery'], function($){

	var $tableBody = $('#receiptsTable tbody')

	function addReceipt(receipt){
		console.log('addReceipt:', receipt)
		var $tr = $('<tr>')
		;['location', 'category', 'price', 'date', 'note'].forEach(function(key){
			var text = receipt[key].toString()
			$('<td>').text(text).appendTo($tr)
		})
		return $tr.appendTo($tableBody)
	}

	$.ajax('/receipts', {
		method: 'GET',
		success: function(receipts){
			receipts.forEach(addReceipt)
		}
	})

	return {
		add: addReceipt
	}
})