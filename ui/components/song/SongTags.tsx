import TagsInput from 'react-tagsinput';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTag, faXmark} from "@fortawesome/free-solid-svg-icons";
import SongTag from "./SongTag";

type SongTags = {
    tags: Array<String>,
    setTags: (tags: Array<string>) => void,
    disabled: boolean,
}

export default function SongTags({tags, setTags, disabled = false}: SongTags) {
    const changeTags = (tags) => {
        setTags(tags)
    }

    const renderTag = (props) => {
        let {tag, disabled, onRemove, getTagDisplayValue} = props
        return (
            <SongTag tag={getTagDisplayValue(tag)} disabled={!disabled} onRemove={onRemove} />
        )
    }

    const renderLayout = (tagComponents, inputComponent) => {
        return (
            <span>
                {inputComponent}
                {tagComponents}
            </span>
        )
    }

    const renderInput = (props) => {
        let {onChange, value, addTag, disabled, ...other} = props
        return (
            <div>
                {!disabled && <input type='text' onChange={onChange} value={value} {...other} />}
            </div>
        )
    }

    return(
        <TagsInput
            value={tags}
            onChange={changeTags}
            inputProps={{className: "form-control mb-2"}}
            tagProps={{className: "badge bg-success m-1"}}
            onlyUnique={true}
            renderTag={renderTag}
            renderLayout={renderLayout}
            renderInput={renderInput}
            disabled={disabled}
        />
    )
}