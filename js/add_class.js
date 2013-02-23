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

// @return true if a department has been chosen and false if the 
//         default of "" has been chosen
function isDeptValid() {

  return $('select[name=dept]').val() !== '';
}

// @return true if number is 5 digits long false otherwise
function isNumValid() {

  return $('input[name=num]').val().length === 5;
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

  var isFormValid = isPhoneValid() && isDeptValid() && isNumValid();

  setSubmitAbility(isFormValid);
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

// Any time the dept drop-down menu in the form changes validate it
$('select[name=dept]').bind(
  'change',
  function() {

    // If validation fails on department menu, post a hint
    // and ensure the submit button is disabled
    if (!isDeptValid()) {

      // If hint not already present add one
      if ($(this).next('span').length == 0) {
        var hint = 'Please select a department before continuing.';
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

// Any time the class number field in the form changes validate it
$('input[name=num]').bind(
  'change',
  function() {

    // If validation fails on class number field, post a hint
    // and ensure the submit button is disabled
    if (!isNumValid()) {

      // If hint not already present add one
      if ($(this).next('span').length == 0) {
        var hint = 'Please enter a class number before continuing (this number is 5 digits and is labeled "Class #" on myUCSC).';
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

