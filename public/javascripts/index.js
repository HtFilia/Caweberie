let index_js = $('script[src*=index]')

let firstSubTitle = index_js.attr('data-firstHighestSubTitle')
let firstSubId = index_js.attr('data-firstHighestSubId')
let secondSubTitle = index_js.attr('data-secondHighestSubTitle')
let secondSubId = index_js.attr('data-secondHighestSubId')
let thirdSubTitle = index_js.attr('data-thirdHighestSubTitle')
let thirdSubId = index_js.attr('data-thirdHighestSubId')
let username = index_js.attr('data-username')


$(function() {
    let navbarNav = document.getElementById("navbar-nav")
    let loggedOutMenu = document.getElementById("logged-out-menu")
    let loggedInMenu = document.getElementById("logged-in-menu")
    let firstLink = navbarNav.children.item(0)
    $(firstLink).children('a')[0].innerHTML = firstSubTitle
    $(firstLink).children('a')[0].setAttribute('href', "/sub/" + firstSubId)
    let secondLink = navbarNav.children.item(1)
    $(secondLink).children('a')[0].innerHTML = secondSubTitle
    $(secondLink).children('a')[0].setAttribute('href', '/sub/' + secondSubId)
    let thirdLink = navbarNav.children.item(2)
    $(thirdLink).children('a')[0].innerHTML = thirdSubTitle
    $(thirdLink).children('a')[0].setAttribute('href', '/sub/' + thirdSubId)
    if (username === "null") {
        loggedInMenu.style.display = "none"
    } else {
        loggedInMenu.children.item(0).children.item(0).innerHTML = username
        loggedOutMenu.style.display = "none"
    }
});