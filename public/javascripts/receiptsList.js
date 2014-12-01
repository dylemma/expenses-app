define(['jquery'], function($){

	var $tableBody = $('#receiptsTable tbody')

	function addReceipt(receipt, flashy){
		var $tr = $('<tr>')
		;['location', 'category', 'price', 'date', 'note'].forEach(function(key){
			var text = receipt[key].toString()
			$('<td>').text(text).appendTo($tr)
		})
		$tr.prependTo($tableBody)

		if(flashy){
			$tr.addClass('flash')
			setTimeout(function(){
				$tr.addClass('flashFade').removeClass('flash')
			}, 1000)
		}

		return $tr
	}

	$.ajax('/receipts', {
		method: 'GET',
		success: function(receipts){
			receipts.forEach(function(receipt){
				addReceipt(receipt, false)
			})
		}
	})

	return {
		add: addReceipt
	}
})