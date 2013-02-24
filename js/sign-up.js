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

// If enabled evaluates to true, make the form submit button enabled
// (clickable), otherwise make it disabled (not clickable).
// @param enabled used as a boolean to determine submit ability
function setSubmitAbility(enabled) {

  var submitButton = $('button[type=submit]');

  // Ensure the submit button is enabled
  if (enabled) {

    // Enable submit button if it is currently disabled
    // (otherwise it must alreday be enabled)
    if (submitButton.attr('disabled')) {
      submitButton.removeAttr('disabled');
    }
  }
  // Ensure the submit button is disabled
  else {

    // Disable submit button if it is currently enabled
    // (otherwise if must already be disabled)
    if (!submitButton.attr('disabled')) {
      submitButton.attr('disabled', 'disabled');
    }
  }
}

// Makes submit button click-able once all input elements have
// valid values and conversely disable the ability to click
// the submit button if one of the fields has become invalid
// where it was previously valid.
function validateForSubmitAbility() {

  var isFormValid = isNameValid() && isEmailValid() && isPhoneValid();
  setSubmitAbility(isFormValid);
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
    // and ensure the submit button is disabled
    if (!isNameValid()) {

      // If hint not already present add one
      if ($(this).next('span').length == 0) {
        var hint = 'Please enter a name before continuing.';
        $(this).after(generateValidationHintSpan(hint));
      }
      setSubmitAbility(false);
    }
    // Otherwise remove the hint and check to see if the whole
    // form is validated and the submit button can be enabled
    else {

      // Remove previously added field hint since field now valid
      if ($(this).next('span')) {
        $(this).next('span').remove();
      }
      validateForSubmitAbility();
    }
  }
);

// Any time the email field in the form changes validate it
$('input[name=email]').bind(
  'change',
  function() {

    // If validation fails on email field, post a hint
    // and ensure the submit button is disabled
    if (!isEmailValid()) {

      // If hint not already present add one
      if ($(this).next('span').length == 0) {
        var hint = 'Please enter a ucsc email (ending in @ucsc.edu) before continuing.';
        $(this).after(generateValidationHintSpan(hint));
      }
      setSubmitAbility(false);
    }
    // Otherwise remove the hint and check to see if the whole
    // form is validated and the submit button can be enabled
    else {

      // Remove previously added field hint since field now valid
      if ($(this).next('span')) {
        $(this).next('span').remove();
      }
      validateForSubmitAbility();
    }
  }
);

// Any time the phone field in the form changes validate it
$('input[name=phone]').bind(
  'change',
  function() {

    // If validation fails on phone field, post a hint
    // and ensure the submit button is disabled
    if (!isPhoneValid()) {

      // If hint not already present add one
      if ($(this).next('span').length == 0) {
        var hint = 'Please enter a phone number with an area code before continuing (for example 831-555-5555).';
        $(this).after(generateValidationHintSpan(hint));
      }
      setSubmitAbility(false);
    }
    // Otherwise remove the hint and check to see if the whole
    // form is validated and the submit button can be enabled
    else {

      // Remove previously added field hint since field now valid
      if ($(this).next('span').length == 1) {
        $(this).next('span').remove();
      }
      validateForSubmitAbility();
    }
  }
);

