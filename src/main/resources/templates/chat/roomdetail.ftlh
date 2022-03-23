<!doctype html>
<html lang="en">
<head>
    <title>Websocket ChatRoom</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/dist/css/bootstrap.min.css">
    <style>
        [v-cloak] {
            display: none;
        }
    </style>
</head>
<body>
<div class="container" id="app" v-cloak>
    <div>
        <h2>{{sender}}</h2>
    </div>
    <div class="input-group">
        <div class="input-group-prepend">
            <label class="input-group-text">내용</label>
        </div>
        <input type="text" class="form-control" v-model="message" @keyup.enter="sendMessage">
        <div class="input-group-append">
            <button class="btn btn-primary" type="button" @click="sendMessage">보내기</button>
        </div>
    </div>
    <ul class="list-group">
        <li class="list-group-item" v-for="message in messages">
            {{message.userNickname}} - {{message.message}}</a>
        </li>
    </ul>
    <div></div>
</div>
<!-- JavaScript -->
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<script src="/webjars/bootstrap/4.3.1/dist/js/bootstrap.min.js"></script>
<script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/2.3.3-1/stomp.min.js"></script>
<script>
    // websocket & stomp initialize
    var sock = new SockJS("/ws-stomp");
    var ws = Stomp.over(sock);
    // vue.js
    var vm = new Vue({
        el: '#app',
        data: {
            chatId: '',
            room: {},
            sender: '',
            message: '',
            messages: [],
            userNickname: ''
        },
        created() {
            this.chatId = localStorage.getItem('wschat.chatId');
            this.sender = localStorage.getItem('wschat.username');
            // this.findRoom();
        },
        methods: {
            // findRoom: function() {
            //     axios.get('/chat/room/'+this.chatId).then(response => { this.room = response.data; });
            // },
            sendMessage: function() {
                ws.send("/pub/chat/message", {}, JSON.stringify({chatRoomId:this.chatId, userId: 8, message:this.message}));
                this.message = '';
            },
            recvMessage: function(recv) {
                this.messages.unshift({"userNickname":recv.userNickname,"message":recv.message})
            }
        }
    });
    // pub/sub event
    ws.connect({}, function(frame) {
        ws.subscribe("/sub/chat/room/"+vm.$data.chatId, function(message) {
            var recv = JSON.parse(message.body);
            console.log("cc")
            console.log(recv)
            vm.recvMessage(recv);
        });
        // ws.send("/pub/chat/message", {}, JSON.stringify({type:'ENTER', chatId:vm.$data.chatId, sender:vm.$data.sender}));
    }, function(error) {
        alert("error "+error);
    });
</script>
</body>
</html>