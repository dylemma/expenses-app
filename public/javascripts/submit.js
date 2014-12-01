require(['bootstrap', 'jquery', 'Bacon', 'underscorejs', './receiptsList'], function(bootstrap, $, Bacon, _, receiptsList){

	window.$ = $
	window._ = _
	window.Bacon = Bacon

	var $expenseForm = $('#expenseForm')

	var inputs = {
		location: { el: $('#inputLocation'), required: true },
		category: { el: $('#inputCategory'), required: true },
		price:    { el: $('#inputPrice'),    required: true },
		date:     { el: $('#inputDate'),     required: true },
		note:     { el: $('#inputNote'),     required: false }
	}

	/*
	Set up the `changes` stream on each of the `inputs` objects.
	*/
	_.forEach(inputs, function(input, key){
		input.changes = input.el
			.asEventStream('input')
			.map(function(){ return input.el.val() })
	})

	/*
	Set up input validation to prohibit blank values on required inputs.
	*/
	_.forEach(inputs, function(input, key){
		if(input.required){
			var $parent = input.el.closest('.form-group')
			input.changes.onValue(function(value){
				$parent.toggleClass('has-success', !!value)
				$parent.toggleClass('has-error', !value)
			})
		}
	})

	/*
	Property that contains a 'view' of the `inputs` object,
	where for each key there is a 'value' and 'required' field.
	*/
	var inputState = Bacon.combineTemplate(
		_.object(_.map(inputs, function(input, key){
			return [key, {
				value: input.changes.toProperty(input.el.val()),
				required: input.required
			}]
		}))
	)

	/*
	Property that contains an object representing what will be
	submitted with the form, but only when all inputs are valid
	(its value will be undefined if any inputs are invalid).
	*/
	var submittableInputs = inputState.map(function(state){
		var requirementsMissing = _.some(state, function(obj, key){
			return obj.required && !obj.value
		})

		if(requirementsMissing){
			return undefined
		} else {
			return _.object(_.map(state, function(obj, key){
				return [key, obj.value]
			}))
		}
	})

	/*
	EventStream of submission events that happen when the form's
	'submit' button is clicked and all of the 'required' inputs
	are filled out. (Note: this prevents the default page reload)
	*/
	var submissionEvents = $expenseForm
		.asEventStream('submit')
		.doAction('.preventDefault')

	var submissions = submittableInputs
		.sampledBy(submissionEvents)
		.filter(_.identity)

	submissions.onValue(function(data){
		console.log('submitting', data)
		$.ajax('/receipts', {
			method: 'POST',
			data: JSON.stringify(data),
			dataType: 'json',
			contentType: 'application/json',
			success: function(receipt){
				receiptsList.add(receipt)
				_.forEach(inputs, function(input){
					input.el.val('')
				})
			}
		})
	})
})