'use strict';

var Game = {};
Game.fps = 0;
Game.aps = 0;
Game.idExec = null;
Game.lastReg = 0;
Game.context = null;
Game.entities = [];

Game.bg = new Image();
Game.bg.src = "rec/bg.png";


function draw_Ent(ctx){
    ctx.drawImage(this.graphics, this.x, this.y);
}

function Ball(){
    this.name = 'Bola';
    this.width = 16;
    this.height = 16;
    this.graphics = new Image(this.width, this.height);
    this.graphics.src = 'rec/fireball.png';
    this.x = 0;
    this.y = 0;
}

Ball.prototype.draw = draw_Ent;

function Metroid(){
    this.name = 'Metroid';
    this.width = 54;
    this.height = 54;
    this.graphics = new Image(this.width, this.height);
    this.graphics.src = 'rec/metroid2.png';
    this.x = 0;
    this.y = 0;
}

Metroid.prototype.draw = draw_Ent;

Metroid.prototype.move = function(direction){
    let step = 8;
    switch (direction) {
        case 37:
            if(this.x >= 0)
                this.x -= step;
            break;
        case 38:
            if(this.y >= 0)
                this.y -= step;
            break;
        case 39:
            if(this.x <= 906)
                this.x += step;
            break;
        case 40:
            if(this.y <= 666)
                this.y += step;
            break;
    }
};


//Bucle principal del juego
Game.loop = function(tempReg){
    Game.idExec = window.requestAnimationFrame(Game.loop);
    Game.refresh(tempReg);
    Game.draw(tempReg);
    
    if(tempReg - Game.lastReg > 999){
        Game.lastReg = tempReg;
        document.getElementById('gameinfo').innerHTML = "<p>APS: "+Game.aps+" FPS: "+Game.fps+"</p>";
        Game.aps = 0;
        Game.fps = 0;
    }
};

Game.stop = function(){
    
};

Game.refresh = function(tempReg){
    //let dir = 37 + Math.floor((41 - 37) * Math.random());
    /*let m1 = Game.entities[1];
    let m0 = Game.entities[0];
    
    if(m1.x + m1.dx > Game.WIDTH-m1.width || m1.x + m1.dx < 0) {
        m1.dx = -m1.dx;
    }
    if(m1.y + m1.dy > Game.HEIGHT-m1.height || m1.y + m1.dy < 0) {
        m1.dy = -m1.dy;
    }
    if((m1.y + m1.height > m0.y && m1.y + m1.height < m0.y + m0.height) && (m1.x + m1.width > m0.x && m1.x + m1.width < m0.x + m0.width)){
        m1.dy = -m1.dy;
        m1.dx = -m1.dx;
        //Console.log('Case 1: A toca a B en x');
    }
    if((m1.y > m0.y && m1.y < m0.y + m0.height) && (m1.x > m0.x && m1.x < m0.x + m0.width)){
        m1.dy = -m1.dy;
        m1.dx = -m1.dx;
        //Console.log('Case 2: A toca a B en y');
    }
    
    m1.x += m1.dx;
    m1.y += m1.dy;*/
    
    Game.aps++;
};

Game.draw = function(tempReg){
    Game.context.clearRect(0, 0, 960, 720);
    Game.context.drawImage(Game.bg, 0, 0);
    /*for (var i = 0, max = Game.entities.length; i < max; i++) {
        Game.entities[i].draw(Game.context);
    }*/
    for(var x in Game.entities){
        Game.entities[x].draw(Game.context);
    }
    //Game.setRect('#FFFFFF');
    Game.fps++;
};

/*Dibujar un cuadrado ------ borrar luego
Game.setRect = function (color){
    this.context.beginPath();
    this.context.rect(Game.WIDTH/2, Game.HEIGHT/2, 50, 50);
    this.context.fillStyle = color;
    this.context.fill();
    this.context.closePath();
};
//----------------------------------*/


Game.addMetroid = function(id, x, y){
    Game.entities[id] = new Metroid();
    Game.entities[id].x = x;
    Game.entities[id].y = y;
};

Game.removeMetroid = function(id) {
    Game.entities[id] = null;
    // Force GC.
    delete Game.entities[id];
};



//------Función con la que inicializa el juego
Game.initialize = function() {
    
    Game.entities['ball'] = new Ball();
    var canvas = document.getElementById('playground');
    if (!canvas.getContext) {
        Console.log('Error: 2d canvas not supported by this browser.');
        return;
    }
    this.context = canvas.getContext('2d');
    
    /*Game.entities[0] = new Metroid();
    Game.entities[1] = new Metroid();
    
    Game.entities[1].x = Game.WIDTH/2;
    Game.entities[1].y = Game.HEIGHT-54;*/
    
    window.addEventListener('keydown', function (e) {
        var code = e.keyCode;
        if (code > 36 && code < 41) {
            Game.socket.send("/move "+code);
        }
    }, false);
    Game.connect('ws://' + window.location.host + '/GameTest/game');
    this.loop();
    Console.log("Game started");
    
};



var Console = {};

Console.log = (function(message) {
    var console = document.getElementById('console');
    var p = document.createElement('p');
    p.style.wordWrap = 'break-word';
    p.innerHTML = message;
    console.appendChild(p);
    while (console.childNodes.length > 25) {
        console.removeChild(console.firstChild);
    }
    console.scrollTop = console.scrollHeight;
});

Game.connect = (function(host) {
    if ('WebSocket' in window) {
        Game.socket = new WebSocket(host);
    } else if ('MozWebSocket' in window) {
        Game.socket = new MozWebSocket(host);
    } else {
        Console.log('Error: WebSocket is not supported by this browser.');
        return;
    }

    Game.socket.onopen = function () {
        // Socket open.. start the game loop.
        Console.log('Info: WebSocket connection opened.');
        /*Game.startGameLoop();
        setInterval(function() {
            // Prevent server read timeout.
            Game.socket.send('ping');
        }, 5000);*/
    };

    Game.socket.onclose = function () {
        Console.log('Info: WebSocket closed.');
        //Game.stopGameLoop();
    };

    Game.socket.onmessage = function (message) {
        //console.log("mensaje: "+message);
        var packet = JSON.parse(message.data);
        console.log(JSON.stringify(packet));
        switch (packet.type) {
            case 'message':
                console.log(JSON.stringify(packet));
                Console.log("<em style='font-weight: bold; color: "+packet.color+";'>"+packet.name+":</em> "+packet.text);
                break;
            case 'join':
                Console.log(packet.id+" has joined");
                for(let p in packet.players){
                    Game.addMetroid(packet.players[p][0], packet.players[p][1], packet.players[p][2]);
                }
                break;  
            case 'leave':
                Console.log(packet.name + " has left the game");
                Game.removeMetroid(packet.id);
                break;
            case 'update':
                for (let d in packet.data) {
                    let ent = Game.entities[packet.data[d][0]];
                    ent.x = packet.data[d][1];
                    ent.y = packet.data[d][2];
                }
                Game.entities['ball'].x = packet.ball[0];
                Game.entities['ball'].y = packet.ball[1];
                break;    

        }
    };
});



Game.initialize();

$("#btnSend").click(sendMessage);

function sendMessage(){
    let msg = $("#msg").val();
    Game.socket.send(msg);
    $("#msg").val("");
}

$("#msg").keypress(function(evt){
    if(evt.keyCode == 13){
        evt.preventDefault();
        sendMessage();
    }
});
