<!doctype html>
<html lang="en">
<head>
    <title>Websocket Chat</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

</head>
<body>
<div class="container" id="app" v-cloak>
    <div class="row">
        <div class="col-md-12">
            <h3>채팅방 리스트</h3>
        </div>
    </div>
    <button type="button" @click="createRoom">클릭 버튼</button>
</div>


<!-- JavaScript -->
<script src="/webjars/vue/2.5.16/dist/vue.min.js"></script>
<script src="/webjars/axios/0.17.1/dist/axios.min.js"></script>
<#--<script src="/webjars/bootstrap/4.3.1/dist/js/bootstrap.min.js"></script>-->
<script src="/webjars/sockjs-client/1.1.2/sockjs.min.js"></script>
<script>
    var vm = new Vue({
        el: '#app',
        data: {
            room_name: '',
            chatrooms: []
        },
        created() {
            this.findAllRoom();
        },
        methods: {
            findAllRoom: function () {
                axios.get('/chat/clickbtn').then(response => {
                    this.chatrooms = response.data;
                });
            },
            createRoom: function () {
                //방 생성
                var params = new URLSearchParams();
                params.append("name", "킥보드를 팝니다.");
                axios.post("/chat/room", JSON.stringify({boardId: 1, buyerId: 8}),{headers:{"Content-Type": `application/json`}})
                    .then(
                        response => {
                            console.log(response.data);
                            alert(response.data.roomname + "방 개설에 성공하였습니다.")
                            // this.room_name = '';
                            // this.findAllRoom();
                            //방 입장
                            localStorage.setItem('wschat.username', "ggg");
                            localStorage.setItem('wschat.chatId', response.data.chatId);
                            location.href = "/chat/room/enter/" + response.data.chatId;
                        }
                    )
                    .catch(response => {
                        alert("채팅방 개설에 실패하였습니다.");
                    });
            },
        }
    });
</script>
</body>
</html>