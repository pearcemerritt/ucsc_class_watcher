// @param elt an input element
// @return true if elt has non-empty value,
//         false otherwise
function isInputValid(elt) {

  if (!elt) {
    return false;
  }

  inputVal = elt.val();
  if (!inputVal || (inputVal.trim().length < 1)) {
    return false;
  }

  return true;
}

// @return true if phone has 3 digits followed by 3 then 4
//         more digits, false otherwise
function isPhoneValid() {

  var elt = $('input[name=phone]');
  if (!isInputValid(elt)) {
    return false;
  }

  return /^.*\d\d\d.*\d\d\d.*\d\d\d\d.*$/.test(elt.val().trim());
}

// @return true if a dept has been chosen and false if the 
//         default of "" has been chosen
function isDeptValid() {

  return $('select[name=dept]').val() !== '';
}

// @return true if number is 5 digits long false otherwise
function isNumValid() {

  return $('input[name=num]').val().length === 5;
}

// Makes submit button click-able once all input elements have
// valid values.
function enableSubmitIfAllInputValid() {

    if (isPhoneValid() && isDeptValid() && isNumValid()) {

      $('button[type=submit]').removeAttr('disabled');
    }
}

// @return an htmlString for a span with hintText as the text
function generateValidationHintSpan(hintText) {
  return '<span class=\'inputHint\'>' + hintText + '</span>';
}

// Any time the phone field in the form changes validate it
$('input[name=phone]').bind(
  'change',
  function() {

    // If validation fails on phone field, post a hint
    if (!isPhoneValid()) {

      // If hint not already present add one
      if ($(this).next('span').length == 0) {
        var hint = 'Please enter a phone number with an area code before continuing (for example 831-555-5555).';
        $(this).after(generateValidationHintSpan(hint));
      }
    }
    // Otherwise remove the hint and check to see if the whole
    // form is validated and the submit button can be enabled
    else {

      // Remove previously added field hint since field now valid
      if ($(this).next('span').length == 1) {
        $(this).next('span').remove();
      }
      enableSubmitIfAllInputValid();
    }
  }
);

