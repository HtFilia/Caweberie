let index_js = $('script[src*=index]')

let firstSubTitle = index_js.attr('data-firstHighestSubTitle')
let firstSubId = index_js.attr('data-firstHighestSubId')
let secondSubTitle = index_js.attr('data-secondHighestSubTitle')
let secondSubId = index_js.attr('data-secondHighestSubId')
let thirdSubTitle = index_js.attr('data-thirdHighestSubTitle')
let thirdSubId = index_js.attr('data-thirdHighestSubId')


$(function() {
    let navbarNav = document.getElementById("navbar-nav")
    let firstLink = navbarNav.children.item(0)
    $(firstLink).children('a')[0].innerHTML = firstSubTitle
    $(firstLink).children('a')[0].setAttribute('href', "/sub/" + firstSubId)
    let secondLink = navbarNav.children.item(1)
    $(secondLink).children('a')[0].innerHTML = secondSubTitle
    $(secondLink).children('a')[0].setAttribute('href', '/sub/' + secondSubId)
    let thirdLink = navbarNav.children.item(2)
    $(thirdLink).children('a')[0].innerHTML = thirdSubTitle
    $(thirdLink).children('a')[0].setAttribute('href', '/sub/' + thirdSubId)
});