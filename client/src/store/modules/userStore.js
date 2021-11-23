import Vue from 'vue';
import http from "@/common/axios.js";
import router from '@/routers/routers.js'

const userStore = {
  namespaced: true,
  state: {
    isAuth: false,
    seq: 0,
    id: '',
    level: 0,
    password: '',
    name: '',
    email: '',
  },

  getters: {
    isAuth: function(state){
      return state.isAuth;
    },
  },

  mutations: {
    SET_USER(state, payload) {
      state.isAuth = true;
      state.seq = payload.seq;
      state.id = payload.id,
      state.level = payload.level;
      state.password = payload.password;
      state.name = payload.name;
      state.email = payload.email;
    },
    SET_USER_LOGOUT(state){
      state.isAuth = false;
      state.seq = 0,
      state.id = '',
      state.level = 0,
      state.password = '',
      state.name = '',
      state.email = ''
    },
    SET_USER_MODIFY(state, payload) {
      state.name = payload.name;
      state.email = payload.email;
    },
    SET_PASSWORD(state, payload) {
      state.password = payload;
    },
  },

  actions: {
    login(context, { userId, userPassword }) {
      http.post(
        "/user/login",
        {
          userId,
          userPassword
        }
      )
      .then((response) => {
        console.log("LoginVue: data : ");
        console.log(response.data);

        const { 
          dto: {
            userId: id,
            userLevel: level,
            userSeq: seq,
            userName: name,
            userPassword: password,
            userEmail: email,
          }
        } = response.data;
        
          context.commit('SET_USER', { seq, id, level, password, name, email });

          router.push("/")
        })
        .catch( error => {
          console.log("LoginVue: error : ");
          console.log(error);

          if (error.response.status == '404'){
            Vue.$swal('아이디 또는 비밀번호를 확인하세요.', { icon: 'error' });
          } else {
            Vue.$swal('서버에 문제가 발생하였습니다.', { icon: 'error' });
          }
        });
      },
    logout(context){
      http.get('/user/logout')
      .then(() => {
        context.commit("SET_USER_LOGOUT");
        router.push('/');
        }).catch(error => {
          console.log(error)
          Vue.$swal('서버에 문제가 발생하였습니다.', { icon: 'error' });
        })
    },
    modifyPassword({ commit }, newPassword) {
      http.put('/user/password', {
          userPassword: newPassword,
        })
        .then(response => {
          if (response.data.result === 1) {
            commit('SET_PASSWORD', newPassword);
            Vue.$swal('비밀번호 변경이 완료되었습니다.', { icon: 'success' })
              .then(() => router.push('/myaccount'));
          }
        })
        .catch(error => {
          console.log("RegisterVue: error : ");
          console.log(error);
          Vue.$swal('서버에 문제가 발생하였습니다.', { icon: 'error' });
        })
    },
  }
}

export default userStore;