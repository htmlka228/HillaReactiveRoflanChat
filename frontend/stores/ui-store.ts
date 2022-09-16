import {
    login as serverLogin,
    logout as serverLogout,
} from '@hilla/frontend';
import {Commands, Router} from "@vaadin/router";

export class UiStore {
    loggedIn = true;

    async login(username: string, password: string) {
        const result = await serverLogin(username, password);
        if (!result.error) {
            this.setLoggedIn(true);
        } else {
            throw new Error(result.errorMessage || 'Login failed');
        }
    }

    async logout() {
        await serverLogout();
        this.setLoggedIn(false);
    }

    setLoggedIn(loggedIn: boolean) {
        this.loggedIn = loggedIn;
        if (loggedIn) {
            Router.go('chat');
        }
    }
}