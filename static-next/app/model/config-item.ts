import { RestModel } from './rest-model';

export class ConfigItem extends RestModel {
    key: string;
    type: string;
    value: string;
    label: string;
}