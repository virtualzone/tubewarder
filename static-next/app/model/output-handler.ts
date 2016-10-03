import { RestModel } from './rest-model';
import { OutputHandlerConfigOption } from './output-handler-config-option';

export class OutputHandler extends RestModel {
    name: string;
    configOptions: OutputHandlerConfigOption[];
}