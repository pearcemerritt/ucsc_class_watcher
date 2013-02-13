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

// @return true if name input field is not just white space,
//         false otherwise
function isNameValid() {
  return isInputValid( $('input[name=name]') );
}

// @return true if email input field is not just white space
//         and contains @ and . in the right order,
//         false otherwise
function isEmailValid() {

  var elt = $('input[name=email]');
  if (!isInputValid(elt)) {
    return false;
  }
  
  return /^\w+@ucsc.edu$/.test(elt.val().trim());
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

// Makes submit button click-able once all input elements have
// valid values.
function enableSubmitIfAllInputValid() {

    if (isNameValid() && isEmailValid() && isPhoneValid()) {

      $('button[type=submit]').removeAttr('disabled');
    }
}

// @return an htmlString for a span with hintText as the text
function generateValidationHintSpan(hintText) {
  return '<span class=\'inputHint\'>' + hintText + '</span>';
}

// Any time the name field in the form changes validate it
$('input[name=name]').bind(
  'change',
  function() {

    // If validation fails on name field, post a hint
    if (!isNameValid()) {

      // If hint not already present add one
      if ($(this).next('span').length == 0) {
        var hint = 'Please enter a name before continuing.';
        $(this).after(generateValidationHintSpan(hint));
      }
    }
    // Otherwise remove the hint and check to see if the whole
    // form is validated and the submit button can be enabled
    else {

      // Remove previously added field hint since field now valid
      if ($(this).next('span')) {
        $(this).next('span').remove();
      }
      enableSubmitIfAllInputValid();
    }
  }
);

// Any time the email field in the form changes validate it
$('input[name=email]').bind(
  'change',
  function() {

    // If validation fails on email field, post a hint
    if (!isEmailValid()) {

      // If hint not already present add one
      if ($(this).next('span').length == 0) {
        var hint = 'Please enter a ucsc email (ending in @ucsc.edu) before continuing.';
        $(this).after(generateValidationHintSpan(hint));
      }
    }
    // Otherwise remove the hint and check to see if the whole
    // form is validated and the submit button can be enabled
    else {

      // Remove previously added field hint since field now valid
      if ($(this).next('span')) {
        $(this).next('span').remove();
      }
      enableSubmitIfAllInputValid();
    }
  }
);

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

