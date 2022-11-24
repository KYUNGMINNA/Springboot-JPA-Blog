let index={

    /*
    fucntion을 사용하려면 이렇게 작성해야 한다 .
    let _this=this;
    init:function (){
        $("#btn-save").on("click",function(){
            _this.save();
        });
    },
    */
    init:function (){
        $("#btn-save").on("click",()=>{ //function(){}  , ()=>{} this를 바인딩하기 위해서 !
            this.save();
        });

        $("#btn-update").on("click",()=>{ //function(){}  , ()=>{} this를 바인딩하기 위해서 !
            this.update();
        });



        /*    구형 로그인 방식
        $("#btn-login").on("click",()=>{ //function(){}  , ()=>{} this를 바인딩하기 위해서 !
            this.login();
        });
        */

    },

    save:function (){
        //alert("save 함수 호출됨");
        let data={
            username:$("#username").val(),
            password:$("#password").val(),
            email:$("#email").val()
        };


        //console.log(data);

        //ajax 호출시 default가 비동기 호출
        //ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청

        //ajax가 통신을 성공하고 서버가 json을 리턴해주면 자동으로 자바 오브젝트로 변환해줌
        //dataType:"json" 을 주석처리하고 사용해도 알아서 변환해준다.
        $.ajax({
            //회원가입 수행 요청
            type:"POST",
            url:"/auth/joinProc",
            data:JSON.stringify(data),  //json 문자열로 변환 --http body 데이터임
            contentType:"application/json;charset=utf-8", //body 데이터가 어떤 타입인지(MIME)
            dataType:"json" //요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 버퍼로와서 문자열
                            //, 생긴게(json)이라면 =>javascript 오브젝트로 변경해줌
        }).done(function (resp){

            if(resp.status===500){
                alert("회원 가입에 실패하였습니다.");
            }else{
            alert("회원가입이 완료되었습니다.");
            location.href="/";
            }

        }).fail(function (error){
            alert(JSON.stringify(error));
        });

    },
    update:function (){
        //alert("save 함수 호출됨");
        let data={
            id:$("#id").val(),
            username: $("#username").val(),
            password:$("#password").val(),
            email:$("#email").val()
        };

        $.ajax({
            //회원가입 수행 요청
            type:"PUT",
            url:"/user",
            data:JSON.stringify(data),  //json 문자열로 변환 --http body 데이터임
            contentType:"application/json;charset=utf-8", //body 데이터가 어떤 타입인지(MIME)
            dataType:"json" //요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 버퍼로와서 문자열
                            //, 생긴게(json)이라면 =>javascript 오브젝트로 변경해줌
        }).done(function (resp){
            alert("회원수정이 완료되었습니다.");
            location.href="/";
        }).fail(function (error){
            alert(JSON.stringify(error));
        });

    }









   /* 구형 로그인 방식
    login:function (){
        //alert("save 함수 호출됨");
        let data={
            username:$("#username").val(),
            password:$("#password").val()
        };

        $.ajax({
            //회원가입 수행 요청
            type:"POST",
            url:"/api/user/login",
            data:JSON.stringify(data),  //json 문자열로 변환 --http body 데이터임
            contentType:"application/json;charset=utf-8", //body 데이터가 어떤 타입인지(MIME)
            dataType:"json" //요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 버퍼로와서 문자열
                            //, 생긴게(json)이라면 =>javascript 오브젝트로 변경해줌
        }).done(function (resp){
            alert("로그인이 완료되었습니다.");
            location.href="/";
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    }
    */
}

index.init();