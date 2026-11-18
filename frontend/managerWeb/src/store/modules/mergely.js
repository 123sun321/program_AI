const mergely = {
  state: {
    assignmentList: []
  },
  mutations: {
    ADD_ASSIGNMENT: (state, assignment) => {
      state.assignmentList.push(assignment)
    }
  },
  actions: {
    addAssignment({ commit }, assignment) {
      commit('ADD_ASSIGNMENT', assignment)
    }
  }
}

export default mergely
