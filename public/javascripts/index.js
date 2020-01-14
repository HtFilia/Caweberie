let index_js = $('script[src*=index]')

var firstSubTitle = index_js.attr('data-firstSubName')
var secondSubTitle = index_js.attr('data-secondSubName')
var thirdSubTitle = index_js.attr('data-thirdSubName')

$(function() {
    let firstLink = document.getElementsByClassName("nav-link")[0]
    firstLink.innerHTML = firstSubTitle
    let secondLink = document.getElementsByClassName("nav-link")[1]
    secondLink.innerHTML = secondSubTitle
    let thirdLink = document.getElementsByClassName("nav-link")[2]
    thirdLink.innerHTML = thirdSubTitle
});