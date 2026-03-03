package com.sicredi.api.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * POJO para a resposta de listagem de usuarios com paginacao.
 *
 * @see <a href="https://dummyjson.com/docs/users">Users API</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserListResponse {

    private List<User> users;
    private Integer total;
    private Integer skip;
    private Integer limit;

    /** @return a lista de usuarios */
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    /** @return o total de usuarios disponiveis */
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    /** @return a quantidade de registros pulados */
    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    /** @return o limite de registros por pagina */
    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
