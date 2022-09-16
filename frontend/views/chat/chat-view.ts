import {html} from 'lit';
import {customElement, state} from 'lit/decorators.js';
import {View} from '../../views/view';
import '@vaadin/vaadin-messages';
import '@vaadin/vaadin-text-field';
import Message from 'Frontend/generated/com/example/application/model/Message';
import {ChatEndpoint, UserInfoEndpoint} from "Frontend/generated/endpoints";
import {TextFieldChangeEvent} from "@vaadin/text-field";

@customElement('chat-view')
export class ChatView extends View {
    @state()
    private messages: Message[] = [];

    @state()
    private username = '';

    render() {
        return html`
            <header slot="navbar" class="w-full flex items-center">
                <vaadin-drawer-toggle></vaadin-drawer-toggle>
                <h1 class="text-l m-m">Roflan Connect</h1>
                <a href="/logout" class="logout-link">Log out</a>
            </header>
            <vaadin-message-list class="flex justify-end flex-grow" .items=${this.messages}></vaadin-message-list>
            <div class="flex p-s gap-s items-baseline">
                <!-- <vaadin-text-field placeholder="Name" @change=${this.usernameChange}></vaadin-text-field> -->
                <vaadin-message-input class="flex-grow" @submit=${this.submit}></vaadin-message-input>
            </div>
        `;
    }

    usernameChange(e: TextFieldChangeEvent) {
        this.username = e.target.value;
    }

    submit(e: CustomEvent) {
        ChatEndpoint.send({
            text: e.detail.value
        }).then(r => r);
    }

    connectedCallback() {
        super.connectedCallback();
        this.classList.add('flex', 'flex-col', 'h-full', 'box-border');

        ChatEndpoint.join().onNext(message => this.messages = [...this.messages, message]);
    }
}
