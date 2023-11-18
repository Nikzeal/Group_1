document.addEventListener("DOMContentLoaded", function() {

const hand = document.querySelector(".clock");
const menu = document.querySelector(".menu-hero");

hand.style.transform = `rotate(0deg)`;

hand.addEventListener("mousemove", (e) => {
    const position = hand.getBoundingClientRect();
    const x = e.x - (position.x + position.width / 2);
    const y = position.y + position.height / 2 - e.y;
    const degrees = -Math.atan2(y, x) * (180 / Math.PI); // Negate the degrees
    hand.style.transform = `rotate(${degrees}deg)`;
});

menu.addEventListener("mousemove", (e) => {
    const position = hand.getBoundingClientRect();
    const x = e.x - (position.x + position.width / 2);
    const y = position.y + position.height / 2 - e.y;
    const degrees = -Math.atan2(y, x) * (180 / Math.PI); // Negate the degrees
    hand.style.transform = `rotate(${degrees}deg)`;
});

(function createSecondLines(){
    var clock = document.querySelector(".menu-hero");
    var rotate = 0;
    
    var byFive = function(n) {
      return (n / 5 === parseInt(n / 5, 10)) ? true : false;
    };
    
    for (i=0; i < 30; i++) {
      var span = document.createElement("span");

      if (byFive(i)) {
        span.className = "fives";
      }
      span.style.transform = "translate(-50%,-50%) rotate("+ rotate + "deg)";
      clock.appendChild(span);
      rotate += 6;
    }
  })();
})