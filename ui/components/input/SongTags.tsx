import TagsInput from 'react-tagsinput';

type SongTags = {
    tags: Array<String>,
    setTags: (tags: Array<string>) => void
}

export default function SongTags({tags, setTags}: SongTags) {
    const changeTags = (tags) => {
        setTags(tags)
    }

    const renderTag = (props) => {
        let {tag, key, disabled, onRemove, classNameRemove, getTagDisplayValue, ...other} = props
        return (
            <span key={key} {...other}>
                {getTagDisplayValue(tag)}
                {!disabled &&
                    <a className="ms-1 text-light remove-tag" onClick={(e) => onRemove(key)}>
                        x
                    </a>
                }
            </span>
        )
    }

    return(
        <TagsInput
            value={tags}
            onChange={changeTags}
            inputProps={{className: "form-control mt-2"}}
            tagProps={{className: "badge bg-success m-1"}}
            onlyUnique={true}
            renderTag={renderTag}
        />
    )
}