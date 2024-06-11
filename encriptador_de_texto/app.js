const keys = new Map([
  ["e", "enter"],
  ["i", "imes"],
  ["a", "ai"],
  ["o", "ober"],
  ["u", "ufat"],
]);

function getDivText(id) {
  return document.getElementById(id).textContent.toLowerCase();
}

function selectText(containerid) {
  window.getSelection().selectAllChildren(document.getElementById(containerid));
}

function noSpecialChars(text) {
  let element = document.getElementById("rules");

  if (/^[a-zA-Z\.\,\!\?\s]*$/g.test(text)) {
    element.classList.remove("invalid");
    return true;
  } else {
    element.classList.add("invalid");
    document.getElementById("decrypted").innerHTML = "";
    return false;
  }
}

function toEncryptOrDecrypt(keysOrValues) {
  if (noSpecialChars(getDivText("toEncrypt"))) {
    document.getElementById("decrypted").innerHTML = transformText(
      getDivText("toEncrypt"),
      keysOrValues
    );
    document.getElementById("copy").style.visibility = "visible";
  }
}

const tooltip = document.getElementById("copyTooltip");

function copiar() {
  navigator.clipboard.writeText(getDivText("decrypted"));
  tooltip.innerHTML = "Copiado!";
  tooltip.style.visibility = "visible";
  tooltip.style.opacity = "1";
}

function outCopyBtn() {
  tooltip.style.visibility = "invisible";
  tooltip.style.opacity = "0";
}

function transformText(text, toSearch) {
  let result = text;

  if (toSearch === "values") {
    for (const value of keys.values()) {
      let temp = "";
      temp = result.replace(new RegExp(value, "g"), getKeyByValue(value));
      result = temp;
    }
  } else if (toSearch === "keys") {
    for (const key of keys.keys()) {
      let temp = "";
      temp = result.replace(new RegExp(key, "g"), keys.get(key));
      result = temp;
    }
  }
  return result;
}

function getKeyByValue(searchValue) {
  for (const [key, value] of keys) {
    if (value === searchValue) return key;
  }
}
