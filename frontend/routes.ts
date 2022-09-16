import {Commands, Context, Route, Router} from '@vaadin/router';
import './views/chat/chat-view';
import './views/login/login-view'
import {autorun} from "mobx";
import { uiStore } from 'Frontend/stores/app-store';

export type ViewRoute = Route & {
    title?: string;
    icon?: string;
    children?: ViewRoute[];
};

export const views: ViewRoute[] = [
    {
        path: '/login',
        component: 'login-view'
    },
    {
        path: 'logout',
        action: (_: Context, commands: Commands) => {
            uiStore.logout();
            return commands.redirect('/login');
        },
    },
    {
        path: '/chat',
        component: 'chat-view',
        action: async (context: Context, commands: Commands) => {
            if (!uiStore.loggedIn) {
                // Save requested path
                sessionStorage.setItem('login-redirect-path', context.pathname);
                return commands.redirect('/login');
            }
            return undefined;
        },
        icon: 'la la-file',
        title: 'Roflan Connect',
    },
];
export const routes: ViewRoute[] = [...views];

autorun(() => {
    if (uiStore.loggedIn) {
        Router.go(sessionStorage.getItem('login-redirect-path') || '/chat');
    } else {
        if (location.pathname !== '/login') {
            sessionStorage.setItem('login-redirect-path', "chat");
            Router.go('/login');
        }
    }
});
