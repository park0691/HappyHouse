import Vue from 'vue';
import http from "@/common/axios.js";
import router from '@/routers/routers.js';

const houseOnGoingStore = {
  namespaced: true,
  state: {
    list: [],
    limit: 8,
    offset: 0,

    // pagination
    listRowCount: 8,
    pageLinkCount: 10,
    currentPageIndex: 1,

    totalListItemCount: 0,

    // detail, update, delete
    ongoingId: 0,
    houseNo: 0,
    AptName: '',
    compName: '',
    compAddress: '',
    compPhone: '',
    compEmail: '',
    title: '',
    content: '',
    dealAmount: '',
    floor: 0,
    area: 0,
    direction: '',
    type: '',
    fee: '',
    room: 0,
    bathroom: 0,
    fileUrl: '',
    fileList: [],
    sameUser: false,

    // search condition
    searchKeyword: '',
    searchKeywordType: 'all',
    searchDealType: '전체',
  },

  getters: {
    getOnGoingCard: function(state){
      return state.list;
    },
    // pagination
    getPageCount: function(state){
      return Math.ceil(state.totalListItemCount/state.listRowCount);
    },
    getStartPageIndex: function(state){
      if( (state.currentPageIndex % state.pageLinkCount) == 0 ){ //10, 20...맨마지막
        return ((state.currentPageIndex / state.pageLinkCount)-1)*state.pageLinkCount + 1
      }else{
        return  Math.floor(state.currentPageIndex / state.pageLinkCount)*state.pageLinkCount + 1
      }
    },
    getEndPageIndex: function(state, getters){
      let ret = 0;
      if( (state.currentPageIndex % state.pageLinkCount) == 0 ){ //10, 20...맨마지막
        ret = ((state.currentPageIndex / state.pageLinkCount)-1)*state.pageLinkCount + state.pageLinkCount;
      }else{
        ret = Math.floor(state.currentPageIndex / state.pageLinkCount)*state.pageLinkCount + state.pageLinkCount;
      }
      // 위 오류나는 코드를 아래와 같이 비교해서 처리
      return ( ret > getters.getPageCount ) ? getters.getPageCount : ret;
    },
    getPrev: function(state){
      if( state.currentPageIndex <= state.pageLinkCount ){
        return false;
      }else{
        return true;
      }
    },
    getNext: function(state, getters){
      if( ( Math.floor( getters.getPageCount / state.pageLinkCount ) * state.pageLinkCount ) < state.currentPageIndex){
        return false;
      }else{
        return true;
      }
    }
  },

  mutations: {
    SET_HOUSE_ONGOING_CARD(state, list){
      list.forEach(item => {
        if (item.fileList) {
          item.fileList.forEach(file => {
            let curUrl = file.fileUrl;
            file.fileUrl = `http://localhost:8080${curUrl}`
          })
        }
      })
      state.list = list
    },
    SET_HOUSE_ONGOING_TOTAL_CARD_ITEM_COUNT(state, count){
      state.totalListItemCount = count
    },
    SET_BOARD_MOVE_PAGE(state, pageIndex){
      state.offset = (pageIndex - 1) * state.listRowCount;
      state.currentPageIndex = pageIndex;
    },
    SET_HOUSE_ONGOING_CARD_DETAIL(state, payload){
      if (payload.fileList) {
        payload.fileList.forEach(file => {
          let curUrl = file.fileUrl;
          file.fileUrl = `http://localhost:8080${curUrl}`
        })
      }

      state.ongoingId = payload.ongoingId
      state.houseNo = payload.houseNo;
      state.AptName = payload.AptName;
      state.compName = payload.compName;
      state.compAddress = payload.compAddress;
      state.compPhone = payload.compPhone;
      state.compEmail = payload.compEmail;
      state.title = payload.title;
      state.content = payload.content;
      state.dealAmount = payload.dealAmount;
      state.floor = payload.floor;
      state.area = payload.area;
      state.direction = payload.direction;
      state.type = payload.type;
      state.fee = payload.fee;
      state.room = payload.room;
      state.bathroom = payload.bathroom;
      state.fileList = payload.fileList;
      state.sameUser = payload.sameUser;
    },
    SET_K(state, payload) {
      state.searchKeyword = payload.keyword;
      state.searchKeywordType = payload.keywordType;
      state.offset = 0;
      state.currentPageIndex = 1;
    },
    SET_DT(state, payload) {
      state.searchDealType = payload;
      state.offset = 0;
      state.currentPageIndex = 1;
    },
    SET_BOOKMARK(state, { enabled, index }) {
      console.log(state.list);
      let newList = state.list.slice();
      console.log(newList);
      newList[index].bookmark = enabled;
      state.list = newList;
    },
  },

  actions: {
    onGoingCard({ commit, state }){
      console.log('list store!!')
      http.get(
        "/house/deal/ongoing",
        {
          params: {
            limit: state.limit,
            offset: state.offset,
            keyword: state.searchKeyword,
            keywordType: state.searchKeywordType,
            dealType: state.searchDealType,
          }
        })
        .then(({ data }) => {
          console.log("HouseOnGoingVue: data : ");
          console.log(data);
          if( data.result == 'login' ){
            router.push("/user/login")
          }else{
            setTimeout(() => {
              commit( 'SET_HOUSE_ONGOING_CARD', data.list );
              commit( 'SET_HOUSE_ONGOING_TOTAL_CARD_ITEM_COUNT', data.count );
            }, 1000)
          }
        })
        .catch((error) => {
          console.log("CardVue: error ");
          console.log(error);
          Vue.$swal('서버에 문제가 발생하였습니다.', { icon: 'error' });
        });
    },
    onGoingDetail({ commit }, ongoingId){
      http.get(`/house/deal/ongoing/${ongoingId}`)
        .then(({ data }) => {
          commit( 'SET_HOUSE_ONGOING_CARD_DETAIL', data.dto);
          router.push("/house/ongoing/detail");
        }
      )
      .catch((error) => {
        console.log("DetailVue: error ");
        console.log(error);
        Vue.$swal('서버에 문제가 발생하였습니다.', { icon: 'error' });
      });
    },
    setBookmark({ commit }, payload) {
      commit('SET_BOOKMARK', payload);
    }
  },
};

export default houseOnGoingStore;